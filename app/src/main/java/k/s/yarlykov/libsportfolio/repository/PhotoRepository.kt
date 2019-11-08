package k.s.yarlykov.libsportfolio.repository

import io.reactivex.Observable
import k.s.yarlykov.libsportfolio.model.Photo
import k.s.yarlykov.libsportfolio.repository.localstorage.ILocalStorage

class PhotoRepository(storage : ILocalStorage) : IPhotoRepository {

    private val photoObservable = storage.connect()

    override fun loadGallery(): Observable<List<Photo>> {
        return photoObservable
    }

    override fun loadFavourites(): Observable<List<Photo>> {
        return photoObservable
            .map {list ->
                list.filter {photo ->
                    photo.isFavorite
                }
            }
    }

    override fun addPhoto(photo: Photo) {
    }

    override fun deletePhoto(id: Int) {
    }

    override fun addToFavourites(id: Int) {
    }
}