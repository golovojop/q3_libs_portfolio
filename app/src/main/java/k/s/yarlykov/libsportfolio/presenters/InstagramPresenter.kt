package k.s.yarlykov.libsportfolio.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import k.s.yarlykov.libsportfolio.logIt
import k.s.yarlykov.libsportfolio.repository.instagram.InstagramAuthHelper
import k.s.yarlykov.libsportfolio.repository.instagram.InstagramGraphHelper
@StateStrategyType(value = SkipStrategy::class)
@InjectViewState
class InstagramPresenter : MvpPresenter<IInstagramFragment>() {

    private lateinit var applicationToken: String
    private val uriImages = mutableListOf<String>()

    fun onViewCreated() {

        if (this::applicationToken.isInitialized) {
            logIt("InstagramPresenter::onViewCreated (has token)")
            loadMediaData(applicationToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mediaDataUriObserver)
        } else {
            logIt("InstagramPresenter::onViewCreated (no token)")
            viewState.showAuthWebPage()
        }
    }

    fun onAppCodeReceived(appCode: String, appSecret: String) {

        viewState.showProgressBar()

        InstagramAuthHelper
            // Получить токен
            .requestToken(appCode, appSecret)
            .subscribeOn(Schedulers.io())
            .doOnNext { token ->
                applicationToken = token.accessToken
            }
            .flatMap { token ->
                loadMediaData(token.accessToken)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(mediaDataUriObserver)
    }

    private fun loadMediaData(token: String): Observable<String> {
        return InstagramGraphHelper
            .requestMediaEdge(token)
            .flatMap { mediaNode ->
                Observable.fromIterable(mediaNode.albums)
            }
            // Получить список медиа ресурсов в альбоме
            .flatMap { mediaAlbum ->
                InstagramGraphHelper.requestMediaData(mediaAlbum.id, applicationToken)
            }
    }

    private val mediaDataUriObserver = object : Observer<String> {
        lateinit var d: Disposable

        override fun onNext(uri: String) {
            logIt("onNext: $uri")
            uriImages.add(uri)
            viewState.updateMediaContent(uriImages)
            viewState.showRecyclerView()
        }

        override fun onSubscribe(d: Disposable) {
            uriImages.clear()
            this.d = d
        }

        override fun onComplete() {
            d.dispose()
            viewState.updateMediaContent(uriImages)
            viewState.showRecyclerView()
            logIt("mediaDataUriObserver: onComplete")
        }

        override fun onError(e: Throwable) {
            logIt("mediaDataUriObserver: onError '${e.message}'")
        }
    }
}