package k.s.yarlykov.libsportfolio.presenters

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
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

    private lateinit var appToken: String
    private lateinit var appSecret: String
    private val uriImages = mutableListOf<String>()
    private val disposable = CompositeDisposable()

    private val dbgPrefix = "${this::class.java.simpleName}::${this.hashCode().toString(16)}"

    override fun onViewCreated(appSecret: String) {
        this.appSecret = appSecret
    }

    override fun onResume() {
        logIt("$dbgPrefix onResume()")

        /**
         * Перенос этого кода из onViewCreated спасает от крэша, потому что
         * TabLayout постоянно пересоздает фрагменты при кликах на заголовках табов.
         * И даже если кликать на зоголовки табов с другими фрагментами все равно
         * пересоздается и InstagramFragment.
         */
        if (this::appToken.isInitialized) {
            logIt("$dbgPrefix [has token], loading media data")

            if(uriImages.size > 0) {
                fragment.updateMediaContent(uriImages)
                fragment.onFrontRecyclerView()
                return
            }

            disposable.add(
                loadMediaData(appToken)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { uri ->
                            uriImages.add(uri)
                            fragment.updateMediaContent(uriImages)
                            fragment.onFrontRecyclerView()
                        },
                        { t: Throwable -> logIt("mediaDataUriObserver: onError '${t.message}'") },
                        {
                            fragment.updateMediaContent(uriImages)
                            fragment.onFrontRecyclerView()
                        })
            )
        } else {
            logIt("$dbgPrefix [no token], start auth")
            fragment.onFrontWebView()
            fragment.showAuthWebPage(authRequestUri)
        }
    }

    override fun onPause() {
        disposable.dispose()
        logIt("$dbgPrefix onPause()")
    }

    // Callback из webview с аутентификацией
    override fun onAppCodeReceived(appCode: String) {

        fragment.onFrontProgressBar()

        authHelper
            // Получить токен
            .requestToken(appCode, appSecret)
            .subscribeOn(Schedulers.io())
            // Сохранить токен
            .doOnNext { token ->
                appToken = token.accessToken
            }
            // Загрузить медиа файлы
            .flatMap { token ->
                loadMediaData(token.accessToken)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(mediaDataUriObserver)
    }

    private fun loadMediaData(token: String): Observable<String> {
        return graphHelper
            .requestMediaEdge(token)
            .flatMap { mediaNode ->
                Observable.fromIterable(mediaNode.albums)
            }
            // Получить список медиа ресурсов в альбоме
            .flatMap { mediaAlbum ->
                graphHelper.requestMediaData(mediaAlbum.id, appToken)
            }
    }

    private val mediaDataUriObserver = object : DisposableObserver<String>() {

        override fun onNext(uri: String) {
            uriImages.add(uri)
            fragment.updateMediaContent(uriImages)
            fragment.onFrontRecyclerView()
        }

        override fun onComplete() {
            fragment.updateMediaContent(uriImages)
            fragment.onFrontRecyclerView()
        }

        override fun onError(e: Throwable) {
            logIt("mediaDataUriObserver: onError '${e.message}'")
        }
    }
}