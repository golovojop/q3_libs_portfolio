package k.s.yarlykov.libsportfolio.presenters

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import k.s.yarlykov.libsportfolio.repository.instagram.InstagramRepo

@InjectViewState
class InstagramPresenter : MvpPresenter<IInstagramFragment>() {

    fun onViewCreated() {
        viewState.showLoading()
    }

    fun onAppCodeReceived(appCode : String, appSecret : String) {

        InstagramRepo
            .requestToken(appCode, appSecret)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("APP_TAG", "token: ${it.accessToken}")
                Log.d("APP_TAG", "user-id: ${it.userId}")
                viewState.showContent()
            }
    }
}