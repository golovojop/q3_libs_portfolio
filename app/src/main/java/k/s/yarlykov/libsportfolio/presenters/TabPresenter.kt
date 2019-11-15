package k.s.yarlykov.libsportfolio.presenters

import io.reactivex.Observable
import k.s.yarlykov.libsportfolio.CONTENT
import k.s.yarlykov.libsportfolio.model.Photo
import k.s.yarlykov.libsportfolio.repository.IPhotoRepository

class TabPresenter(private val repository : IPhotoRepository) {

    private lateinit var content : CONTENT

    fun setContentType(content : CONTENT) {
        this.content = content
    }

    fun getPhotoObservable() : Observable<List<Photo>> {

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