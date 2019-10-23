package k.s.yarlykov.libsportfolio.presenters

import k.s.yarlykov.libsportfolio.contracts.MainContract

class MainPresenter() : MainContract.IMainPresenter {

    private var view : MainContract.IMainView? = null

    override fun onFabTapped() {

    }

    override fun bind(view: MainContract.IMainView) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }
}