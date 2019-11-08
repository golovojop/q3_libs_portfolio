package k.s.yarlykov.libsportfolio.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import k.s.yarlykov.libsportfolio.logIt
import k.s.yarlykov.libsportfolio.repository.instagram.InstagramAuthHelper
import k.s.yarlykov.libsportfolio.repository.instagram.InstagramGraphHelper

@InjectViewState
class InstagramPresenter : MvpPresenter<IInstagramFragment>() {

    private lateinit var appToken: String
    private lateinit var appSecret: String
    private val uriImages = mutableListOf<String>()

    fun onViewCreated(appSecret: String) {

        this.appSecret = appSecret

        if (this::appToken.isInitialized) {
            logIt("InstagramPresenter::onViewCreated (has token)")
            loadMediaData(appToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mediaDataUriObserver)
        } else {
            logIt("InstagramPresenter::onViewCreated (no token)")
            viewState.onFrontWebView()
            viewState.showAuthWebPage()
        }
    }

    // Callback из webview с аутентификацией
    fun onAppCodeReceived(appCode: String) {

        if (!this::appToken.isInitialized) {
            viewState.onFrontProgressBar()

            InstagramAuthHelper
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
        return InstagramGraphHelper
            .requestMediaEdge(token)
            .flatMap { mediaNode ->
                Observable.fromIterable(mediaNode.albums)
            }
            // Получить список медиа ресурсов в альбоме
            .flatMap { mediaAlbum ->
                InstagramGraphHelper.requestMediaData(mediaAlbum.id, appToken)
            }
    }

    private val mediaDataUriObserver = object : Observer<String> {
        lateinit var d: Disposable

        override fun onNext(uri: String) {
            logIt("onNext: $uri")
            uriImages.add(uri)
            viewState.updateMediaContent(uriImages)
            viewState.onFrontRecyclerView()
        }

        override fun onSubscribe(d: Disposable) {
            uriImages.clear()
            this.d = d
        }

        override fun onComplete() {
            d.dispose()
            viewState.updateMediaContent(uriImages)
            viewState.onFrontRecyclerView()
            logIt("mediaDataUriObserver: onComplete")
        }

        override fun onError(e: Throwable) {
            logIt("mediaDataUriObserver: onError '${e.message}'")
        }
    }
}