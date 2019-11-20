package k.s.yarlykov.libsportfolio.repository

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import k.s.yarlykov.libsportfolio.domain.room.Photo
import k.s.yarlykov.libsportfolio.logIt
import k.s.yarlykov.libsportfolio.repository.localstorage.ILocalStorage
import k.s.yarlykov.libsportfolio.repository.room.IRoomRepo

/**
 * Работает как интерактор между локальным кэшем (LocalStorage) и СУБД (Room)
 */

class PhotoRepository(private val cashRepo: ILocalStorage, private val daoRepo: IRoomRepo) :
    IPhotoRepository {

    init {
        cashRepo.populateCache()
    }

    private val photoSource by lazy(LazyThreadSafetyMode.NONE) {
        cashRepo.connect()
    }

    override fun loadGallery(): Observable<List<Photo>> =
        photoWithMetaDataObservable()
            .observeOn(AndroidSchedulers.mainThread())

    override fun loadFavourites(): Observable<List<Photo>> =
        photoWithMetaDataObservable()
            .map { list ->
                list.filter { photo ->
                    photo.favorite
                }
            }
            .observeOn(AndroidSchedulers.mainThread())


    override fun addPhoto(photo: Photo) {
    }

    override fun deletePhoto(id: Int) {
    }

    override fun addToFavourites(id: Int) {
    }

    private fun photoWithMetaDataObservable(): Observable<List<Photo>> =
        photoSource
            .flatMapObservable { list ->
                Observable.fromIterable(list)
            }
            .flatMapMaybe { rawPhoto ->

                daoRepo
                    .getPhoto(rawPhoto.id)
                    .defaultIfEmpty(rawPhoto)
                    .map { photoMetaData ->
                        photoMetaData.copy(bitmap = rawPhoto.bitmap)
                    }
                    .doOnSuccess {photo ->
                        logIt("photoWithMetaDataObservable::withMetaData ${photo.id}")
                        daoRepo.insertPhoto(photo)
                    }
            }
            .doOnNext{
                logIt("photoWithMetaDataObservable::doOnNext ${it.bitmap.toString()}")
            }
            .doOnComplete{
                logIt("photoWithMetaDataObservable::doOnComplete")
            }
            .toList()
            .doOnSuccess{
                logIt("photoWithMetaDataObservable::toList success ${it.size}")
            }
            .toObservable()
}