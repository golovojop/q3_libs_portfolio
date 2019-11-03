package k.s.yarlykov.libsportfolio.repository

import io.reactivex.Observable
import k.s.yarlykov.libsportfolio.model.Photo


interface ILocalStorage {
    fun connect(): Observable<List<Photo>>
    fun addPhoto(photo: Photo)
    fun deletePhoto(photo: Photo)
}