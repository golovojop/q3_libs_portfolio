package k.s.yarlykov.libsportfolio.contracts

import io.reactivex.Observable
import k.s.yarlykov.libsportfolio.model.Photo

interface RepositoryContract {

    interface IPhotoRepository {
        fun getGallery() : Observable<Photo>
        fun getFavourites() : Observable<Photo>
    }
}