package k.s.yarlykov.libsportfolio.repository

import io.reactivex.Observable
import k.s.yarlykov.libsportfolio.domain.room.Photo

interface IPhotoRepository {
    fun galleryStream() : Observable<List<Photo>>
    fun favoritesStream() : Observable<List<Photo>>
    fun onUpdate(position: Int, photo: Photo)
    fun onDisconnect()
}
