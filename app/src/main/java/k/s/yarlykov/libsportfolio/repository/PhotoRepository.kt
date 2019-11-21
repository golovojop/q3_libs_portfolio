package k.s.yarlykov.libsportfolio.repository

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import k.s.yarlykov.libsportfolio.domain.room.Photo
import k.s.yarlykov.libsportfolio.logIt
import k.s.yarlykov.libsportfolio.repository.localstorage.ILocalStorage
import k.s.yarlykov.libsportfolio.repository.room.IRoomRepo

/**
 * Работает как интерактор между локальным кэшем (LocalStorage),
 * СУБД (Room) и презентерами
 */

class PhotoRepository(
    private val cashRepo: ILocalStorage,
    private val daoRepo: IRoomRepo
) :
    IPhotoRepository {

    init {
        cashRepo.populateCache()
    }

    private val photoSource by lazy(LazyThreadSafetyMode.NONE) {
        cashRepo.connect()
    }

    override fun loadGallery(): Observable<List<Photo>> {
        return photoWithMetaDataObservable()
            .observeOn(AndroidSchedulers.mainThread())
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

        return photoSource
            .flatMapObservable { list ->
                Observable.fromIterable(list)
            }
            .flatMapMaybe { rawPhoto ->

                // Далее нужно заполнить rawPhoto метеданными (лайки, избранное) из СУБД
                daoRepo
                    // Запросить данные в базе
                    .getPhoto(rawPhoto.id)
                    // Если данных нет, то берем пустые из rawPhoto
                    .defaultIfEmpty(rawPhoto)
                    // Копируем в результирующую фотку битмапу из rawPhoto
                    .map { photoMetaData ->
                        photoMetaData.copy(bitmap = rawPhoto.bitmap)
                    }
                    // Результирующую фотку помещаем в базу
                    .doOnSuccess { photo ->
                        daoRepo.insertPhoto(photo)
                    }
            }
            // Все фотки из flatMapMaybe собираем в List
            .toList()
            .toObservable()
    }
}