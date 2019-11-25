package k.s.yarlykov.libsportfolio.repository

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import k.s.yarlykov.libsportfolio.domain.room.Photo
import k.s.yarlykov.libsportfolio.repository.localstorage.ILocalStorage
import k.s.yarlykov.libsportfolio.repository.room.IRoomRepo

/**
 * Работает как интерактор между локальным кэшем (LocalStorage),
 * СУБД (Room) и презентерами
 */

class PhotoRepository(
    cashRepo: ILocalStorage,
    private val daoRepo: IRoomRepo
) :
    IPhotoRepository {

    private val liveData = BehaviorSubject.create<List<Photo>>()

    init {
        cashRepo.populateCache()

        // Собрать Bitmap'ы картинок и метаданные в объекты Photo.
        // Результирующий массив эмиттировать через liveData.
        assembleRawBitmapsWithMetadata(cashRepo.connectToBitmapStream())
            .subscribe { photos ->
                liveData.onNext(photos)
            }

        // Подписаться на изменения состояний Likes/Favorites, чтобы сохранять их в БД.
        // Для работы с БД в отдельном потоке создать независимый Observable в flatMap.
        liveData
            .flatMap { photos ->
                Observable.just(photos)
                    .subscribeOn(Schedulers.io())
            }
            .subscribe { photos ->
                daoRepo.insertPhotos(photos)
            }
    }

    override fun galleryStream(): Observable<List<Photo>> = liveData.hide()
    override fun favoritesStream(): Observable<List<Photo>> =
        liveData
            .hide()
            .map { list ->
                list.filter { photo ->
                    photo.favorite
                }
            }

    override fun onUpdate(position: Int, photo: Photo) {

        // Заменить элемент в списке
        liveData.onNext(
            liveData.value.map { p ->
                if (p.id == photo.id) photo else p
            }
        )
    }

    private fun assembleRawBitmapsWithMetadata(rawPhotos: Single<List<Photo>>): Single<List<Photo>> =
        rawPhotos
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
}