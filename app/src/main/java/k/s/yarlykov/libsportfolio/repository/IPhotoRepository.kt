package k.s.yarlykov.libsportfolio.repository

import io.reactivex.Observable
import k.s.yarlykov.libsportfolio.domain.room.Photo

interface IPhotoRepository {
    fun loadGallery() : Observable<List<Photo>>
    fun loadFavourites() : Observable<List<Photo>>
    fun addPhoto(photo: Photo)
    fun deletePhoto(id : Int)
    fun addToFavourites(id : Int)
}
