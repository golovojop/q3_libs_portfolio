package k.s.yarlykov.libsportfolio.contracts

import io.reactivex.Observable
import k.s.yarlykov.libsportfolio.model.Photo

interface StorageContract {
    interface ILocalStorage {
        fun connect(): Observable<List<Photo>>
        fun addPhoto(photo: Photo)
        fun deletePhoto(photo: Photo)
    }
}