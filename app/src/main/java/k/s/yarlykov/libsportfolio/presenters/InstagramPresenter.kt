package k.s.yarlykov.libsportfolio.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.MaybeObserver
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import k.s.yarlykov.libsportfolio.logIt
import k.s.yarlykov.libsportfolio.repository.instagram.InstagramAuthHelper
import k.s.yarlykov.libsportfolio.repository.instagram.InstagramGraphHelper

@InjectViewState
class InstagramPresenter : MvpPresenter<IInstagramFragment>() {

    private lateinit var applicationToken: String
    private val uriImages = mutableListOf<String>()

    fun onViewCreated() {
        viewState.showAuthPage()
    }

    fun onAppCodeReceived(appCode: String, appSecret: String) {

        viewState.showProgressBar()

        InstagramAuthHelper
                // Получить токен
            .requestToken(appCode, appSecret)
            .subscribeOn(Schedulers.io())
            .doOnNext{
                logIt("token: ${it.accessToken}")
            }
                // Получить корневую ноду дерева медиаресурсов
            .flatMap { token ->
                applicationToken = token.accessToken
                InstagramGraphHelper.requestMediaEdge(applicationToken)
            }
                // Получить список альбомов
            .flatMap { mediaNode ->
                Observable.fromIterable(mediaNode.data)
            }
                // Получить список медиа ресурсов в альбоме
            .flatMap { mediaAlbum ->
                InstagramGraphHelper.requestMediaData(mediaAlbum.id, applicationToken)
            }
                // Вернуть
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(mediaDataUriObserver)
    }

    private val mediaDataUriObserver = object : Observer<String> {
        lateinit var d: Disposable

        override fun onNext(uri : String) {
            logIt("onNext: $uri")
            uriImages.add(uri)
            viewState.updateMediaContent(uriImages)
            viewState.showRecyclerView()
//            viewState.loadMediaContent(uri)
//            viewState.showWebView()
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