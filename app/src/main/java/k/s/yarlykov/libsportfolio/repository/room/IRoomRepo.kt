package k.s.yarlykov.libsportfolio.repository.room

import io.reactivex.Maybe
import k.s.yarlykov.libsportfolio.domain.room.Photo

interface IRoomRepo {
    fun getPhoto(): Maybe<List<Photo>>
    fun getPhoto(id: Int): Maybe<Photo>
    fun insertPhoto(photo: Photo)
    fun insertPhotos(photos: List<Photo>)
}