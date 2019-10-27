package k.s.yarlykov.libsportfolio.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import k.s.yarlykov.libsportfolio.model.Photo
import k.s.yarlykov.libsportfolio.repository.IPhotoRepository

@InjectViewState
class TabPresenter : MvpPresenter<ITabFragment>() {
    private lateinit var repository : IPhotoRepository

    fun setPhotoRepository(repository : IPhotoRepository) {
        this.repository = repository

        val d = repository.loadGallery()
            .subscribe(::updateView)
    }

    private fun updateView(photos: List<Photo>) {
        viewState.updateContent(photos)
    }
}