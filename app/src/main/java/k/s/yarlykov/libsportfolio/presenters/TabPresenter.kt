package k.s.yarlykov.libsportfolio.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Observable
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

    fun setPhotoRepository(repository : IPhotoRepository) : Observable<List<Photo>> {
        this.repository = repository

        return when(content) {
            CONTENT.FAVORITES -> repository.loadFavourites()
            CONTENT.GALLERY -> repository.loadGallery()

            /**
             * Temporary
             */
            CONTENT.INSTAGRAM -> repository.loadGallery()
        }
    }
}