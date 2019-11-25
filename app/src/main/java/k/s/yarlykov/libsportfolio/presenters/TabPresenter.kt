package k.s.yarlykov.libsportfolio.presenters

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import k.s.yarlykov.libsportfolio.CONTENT
import k.s.yarlykov.libsportfolio.logIt
import k.s.yarlykov.libsportfolio.model.Photo
import k.s.yarlykov.libsportfolio.repository.IPhotoRepository

class TabPresenter(
    private val fragment: ITabFragment,
    private val repository: IPhotoRepository
) : ITabPresenter {

    private lateinit var content: CONTENT

    private lateinit var disposable: Disposable

    override fun onViewCreated(content: CONTENT) {
        this.content = content
        disposable = getPhotoObservable().subscribe{photos ->
            fragment.updateContent(photos)
        }
    }

    override fun onDestroyView() {
        if(!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    private fun getPhotoObservable(): Observable<List<Photo>> {

        return when (content) {
            CONTENT.FAVORITES -> repository.loadFavourites()
            CONTENT.GALLERY -> repository.loadGallery()

            /**
             * Temporary
             */
            CONTENT.INSTAGRAM -> repository.loadGallery()
        }
    }
}