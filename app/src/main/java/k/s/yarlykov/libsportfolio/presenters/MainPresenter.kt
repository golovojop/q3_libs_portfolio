package k.s.yarlykov.libsportfolio.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import k.s.yarlykov.libsportfolio.contracts.MainContract

@InjectViewState
class MainPresenter : MvpPresenter<IMainView>() {

    private var view : MainContract.IMainView? = null

    fun onFabTapped() {

    }

}