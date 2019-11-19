package k.s.yarlykov.libsportfolio.repository.localstorage

import io.reactivex.Observable
import k.s.yarlykov.libsportfolio.domain.room.Photo


interface ILocalStorage {
    fun connect(): Observable<List<Photo>>
    fun addPhoto(photo: Photo)
    fun deletePhoto(photo: Photo)
    fun doUpload()
}