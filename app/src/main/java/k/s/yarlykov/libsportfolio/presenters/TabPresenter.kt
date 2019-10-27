package k.s.yarlykov.libsportfolio.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import k.s.yarlykov.libsportfolio.CONTENT
import k.s.yarlykov.libsportfolio.model.Photo
import k.s.yarlykov.libsportfolio.repository.IPhotoRepository

@InjectViewState
class TabPresenter : MvpPresenter<ITabFragment>() {
    private lateinit var repository : IPhotoRepository
    private lateinit var content : CONTENT

    fun setContentType(content : CONTENT) {
        this.content = content
    }

    fun setPhotoRepository(repository : IPhotoRepository) {
        this.repository = repository

        val disposable = when(content) {
            CONTENT.FAVOURITES -> repository.loadFavourites()
            CONTENT.GALLERY -> repository.loadGallery()
        }.subscribe(::updateView)
    }

    private fun updateView(photos: List<Photo>) {
        viewState.updateContent(photos)
    }
}