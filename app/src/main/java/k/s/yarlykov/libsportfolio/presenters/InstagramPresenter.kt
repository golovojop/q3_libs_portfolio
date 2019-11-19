package k.s.yarlykov.libsportfolio.presenters

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
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


    override fun onViewCreated(appSecret: String) {

        this.appSecret = appSecret

        if (this::appToken.isInitialized) {
            logIt("InstagramPresenter::onViewCreated (has token)")
            loadMediaData(appToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mediaDataUriObserver)
        } else {
            logIt("InstagramPresenter::onViewCreated (no token)")
            fragment.onFrontWebView()
            fragment.showAuthWebPage(authRequestUri)
        }
    }

    // Callback из webview с аутентификацией
    override fun onAppCodeReceived(appCode: String) {

        if (!this::appToken.isInitialized) {
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

    private val mediaDataUriObserver = object : Observer<String> {
        lateinit var d: Disposable

        override fun onNext(uri: String) {
            logIt("onNext: $uri")
            uriImages.add(uri)
            fragment.updateMediaContent(uriImages)
            fragment.onFrontRecyclerView()
        }

        override fun onSubscribe(d: Disposable) {
            uriImages.clear()
            this.d = d
        }

        override fun onComplete() {
            d.dispose()
            fragment.updateMediaContent(uriImages)
            fragment.onFrontRecyclerView()
            logIt("mediaDataUriObserver: onComplete")
        }

        override fun onError(e: Throwable) {
            logIt("mediaDataUriObserver: onError '${e.message}'")
        }
    }
}