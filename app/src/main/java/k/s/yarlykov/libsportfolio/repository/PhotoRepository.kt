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

    override fun loadGallery(): Observable<List<Photo>> {
        logIt("loadGallery")
        return photoWithMetaDataObservable()
            //.observeOn(AndroidSchedulers.mainThread())
    }

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

    private fun photoWithMetaDataObservable(): Observable<List<Photo>> {

        logIt("photoWithMetaDataObservable count = ${++count}")

        return photoSource
            .flatMapObservable { list ->
                logIt(list.toString())
                Observable.fromIterable(list)
            }
            .flatMapMaybe { rawPhoto ->

                daoRepo
                    .getPhoto(rawPhoto.id)
                    .doOnError {
                        logIt("1. daoRepo::getPhoto::doOnError ${it.message}")
                    }
                    .doOnSuccess {
                        logIt("2. daoRepo::getPhoto::doOnSuccess photo id = ${it.id}")
                    }
                    .defaultIfEmpty(rawPhoto)
                    .map { photoMetaData ->
                        logIt("3. map")
                        photoMetaData.copy(bitmap = rawPhoto.bitmap)
                    }
                    .doOnSuccess { photo ->
                        logIt("4. map::doOnSuccess ${photo.id}")
                        daoRepo.insertPhoto(photo)
                    }
            }
            .doOnNext {
                logIt("5. flatMapMaybe::doOnNext ${it.bitmap.toString()}")
            }
            .doOnComplete {
                logIt("6. flatMapMaybe::doOnComplete")
            }
            .toList()
            .doOnSuccess {
                logIt("7. toList::doOnSuccess ${it.size}")
            }
            .toObservable()
    }


    companion object {
        var count : Int = 0
    }
}