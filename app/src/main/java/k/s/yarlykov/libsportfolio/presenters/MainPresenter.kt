package k.s.yarlykov.libsportfolio.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

@InjectViewState
class MainPresenter : MvpPresenter<IMainView>() {

    fun onFabTapped() {
        viewState.onClickFabHandler()
    }

}