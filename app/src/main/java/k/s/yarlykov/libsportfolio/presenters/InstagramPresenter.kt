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

    private lateinit var applicationToken: String

    fun onViewCreated() {
        viewState.startLoading()
    }

    fun onAppCodeReceived(appCode: String, appSecret: String) {
        viewState.showLoading()

        logIt("code : $appCode")

        InstagramAuthHelper
            .requestToken(appCode, appSecret)
            .subscribeOn(Schedulers.io())
            .flatMap { token ->
                applicationToken = token.accessToken
                InstagramGraphHelper.requestMediaEdge(applicationToken)
            }
            .flatMap { mediaNode ->
                Observable.fromIterable(mediaNode.data)
            }
            .flatMap { mediaAlbum ->
                InstagramGraphHelper.requestMediaData(mediaAlbum.id, applicationToken)
            }
            .take(1)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(mediaDataUriObserver)
    }


    private val mediaDataUriObserver = object : Observer<String> {

        lateinit var d: Disposable

        override fun onComplete() {
            d.dispose()
            viewState.showContent()
        }

        override fun onSubscribe(d: Disposable) {
            this.d = d
        }

        override fun onNext(uri: String) {
            viewState.showMedia(uri)
        }

        override fun onError(e: Throwable) {
            logIt("mediaDataUriObserver:: ${e.message}")
        }
    }
}