package k.s.yarlykov.libsportfolio.repository.localstorage

import io.reactivex.Single
import k.s.yarlykov.libsportfolio.domain.room.Photo

interface ILocalStorage {
    fun connectToBitmapStream(): Single<List<Photo>>
    fun addPhoto(photo: Photo)
    fun deletePhoto(photo: Photo)
    fun populateCache()
}