package k.s.yarlykov.libsportfolio.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import k.s.yarlykov.libsportfolio.repository.IPhotoRepository

@InjectViewState
class MainPresenter : MvpPresenter<IMainView>() {

    private lateinit var repository : IPhotoRepository

    fun setPhotoRepository(repository : IPhotoRepository) {
        this.repository = repository

    }

    fun onFabTapped() {
        viewState.onClickFabHandler()
    }

}