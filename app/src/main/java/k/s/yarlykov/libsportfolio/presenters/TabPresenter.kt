package k.s.yarlykov.libsportfolio.presenters

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import k.s.yarlykov.libsportfolio.CONTENT
import k.s.yarlykov.libsportfolio.domain.room.Photo
import k.s.yarlykov.libsportfolio.logIt
import k.s.yarlykov.libsportfolio.repository.IPhotoRepository

class TabPresenter(
    private val fragment: ITabFragment,
    private val repository: IPhotoRepository
) : ITabPresenter {

    private lateinit var content: CONTENT

    private lateinit var disposable: Disposable

    override fun onViewCreated(content: CONTENT) {
        this.content = content

//        val bbb : io.reactivex.internal.operators.single.SingleToObservable
//        val ccc : io.reactivex.internal.operators.observable.ObservableObserveOn

        val d = getPhotoObservable()
        logIt("${d::class.java.canonicalName}")

            d.subscribe { photos ->
                logIt("TabPresenter subscribed")
                fragment.updateContent(photos)
            }
    }

    override fun onDestroyView() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    private fun getPhotoObservable(): Observable<List<Photo>> {

        return when (content) {
            CONTENT.GALLERY -> repository.loadGallery()
            CONTENT.FAVORITES -> repository.loadFavourites()

            /**
             * Temporary
             */
            CONTENT.INSTAGRAM -> repository.loadGallery()
        }
    }
}