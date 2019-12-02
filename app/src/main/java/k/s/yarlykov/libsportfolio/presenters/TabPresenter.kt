package k.s.yarlykov.libsportfolio.presenters

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import k.s.yarlykov.libsportfolio.CONTENT
import k.s.yarlykov.libsportfolio.domain.room.Photo
import k.s.yarlykov.libsportfolio.repository.IPhotoRepository

class TabPresenter(
    private val fragment: ITabFragment,
    private val repository: IPhotoRepository
) : ITabPresenter {

    private lateinit var content: CONTENT

    private lateinit var disposable: Disposable

    private val model = mutableListOf<Photo>()

    override fun onViewCreated(content: CONTENT) {
        this.content = content

        disposable = getPhotoObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { photos ->
                replaceModel(photos)
                fragment.updateContent(model)
            }
    }

    override fun onDestroyView() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    override fun onClickLikeButton(position: Int) {
        with(model[position]){
            likes++
            repository.onUpdate(position, this)
        }
    }

    override fun onClickFavoriteButton(position: Int) {
        with(model[position]){
            favorite = !favorite
            repository.onUpdate(position, this)
        }
    }

    private fun getPhotoObservable(): Observable<List<Photo>> {

        return when (content) {
            CONTENT.GALLERY -> repository.galleryStream()
            CONTENT.FAVORITES -> repository.favoritesStream()

            /**
             * Temporary
             */
            CONTENT.INSTAGRAM -> repository.galleryStream()
        }
    }

    // Обновить локальную копию данных
    private fun replaceModel(photos: List<Photo>) {
        with(model) {
            clear()
            addAll(photos)
        }
    }
}