package k.s.yarlykov.libsportfolio.presenters

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import k.s.yarlykov.libsportfolio.logIt
import k.s.yarlykov.libsportfolio.repository.instagram.IInstagramAuthHelper
import k.s.yarlykov.libsportfolio.repository.instagram.IInstagramGraphHelper

class InstagramPresenter(
    private val fragment: IInstagramFragment,
    private val authHelper: IInstagramAuthHelper,
    private val graphHelper: IInstagramGraphHelper,
    private val authRequestUri: String
) : IInstagramPresenter {

    private lateinit var appSecret: String
    private lateinit var appCode: String
    private lateinit var appToken: String

    private val uriImages = mutableListOf<String>()
    private val disposable = CompositeDisposable()

    private val dbgPrefix = "${this::class.java.simpleName}::${this.hashCode().toString(16)}"

    // Step 3.
    private fun getMediaDataObservable(): Observable<String> {

        return getTokenObservable()
            .doOnSuccess {
                logIt("getMediaDataObservable:getTokenObservable")
            }
            .flatMapObservable { token ->
                graphHelper.requestMediaEdge(token)
            }
            .doOnNext {
                logIt("getMediaDataObservable:flatMap 1")
            }

            .flatMap { mediaNode ->
                Observable.fromIterable(mediaNode.albums)
            }
            .doOnNext {
                logIt("getMediaDataObservable:flatMap 2")
            }
            // Получить список медиа ресурсов в альбоме
            .flatMap { mediaAlbum ->
                graphHelper.requestMediaData(mediaAlbum.id, appToken)
            }
            .doOnNext {
                logIt("getMediaDataObservable:flatMap 3. exit")
            }
    }

    // Step 2.
    private fun getTokenObservable(): Single<String> {
        logIt("getTokenObservable [enter]")

        return if (this::appToken.isInitialized) {
            logIt("getTokenObservable [has token]")
            Single.fromCallable {
                appToken
            }
        } else {
            logIt("getTokenObservable [no token]")
            authHelper
                .requestToken(appCode, appSecret)
                .map { token ->
                    token.accessToken
                }
                .doOnSuccess { t ->
                    appToken = t
                }
        }
    }

    // Step 1.
    override fun onAppCodeReceived(applicationCode: String) {
        logIt("onAppCodeReceived [$applicationCode]")

        if (this::appCode.isInitialized &&
            this::appToken.isInitialized &&
            uriImages.isNotEmpty()
        ) {
            updateView()
            return
        }

        this.appCode = applicationCode

        fragment.onFrontProgressBar()

        disposable.add(
            getMediaDataObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { logIt("loaded next") }
                .doOnComplete { logIt("loading complete") }
                .subscribe(
                    { uri -> uriImages.add(uri) },
                    { t: Throwable -> logIt("MediaDataObservable::onError '${t.message}'") },
                    { updateView() })
        )
    }

    override fun onResume() {
        logIt("$dbgPrefix onResume()")

        /**
         * Перенос этого кода из onViewCreated спасает от крэша, потому что
         * TabLayout постоянно пересоздает фрагменты при кликах на заголовках табов.
         * И даже если кликать на зоголовки табов с другими фрагментами все равно
         * пересоздается и InstagramFragment.
         */

        // Контент уже получен. Вывести на экран
        if (uriImages.isNotEmpty()) {
            updateView()

            // Контента нет, но уже получен ApplicationCode.
            // Значит можно получить токен, а затем контент
        } else if (this::appCode.isInitialized) {
            onAppCodeReceived(appCode)

            // Ещё не получен ApplicationCode.
            // Запросить аутентификацию через GUI
        } else {
            fragment.onFrontWebView()
            fragment.showAuthWebPage(authRequestUri)
        }
    }

    override fun onViewCreated(appSecret: String) {
        this.appSecret = appSecret
    }

    override fun onPause() {
        logIt("$dbgPrefix onPause()")
        disposable.clear()
    }

    private fun updateView() {
        fragment.updateMediaContent(uriImages)
        fragment.onFrontRecyclerView()
    }
}