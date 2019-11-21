package k.s.yarlykov.libsportfolio

import android.graphics.Bitmap
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import k.s.yarlykov.libsportfolio.domain.room.Photo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PhotoRepositoryTest {

    var bitmap: Bitmap = mock(Bitmap::class.java)

    val rawPhotos = listOf(
        Photo(10, "", false, 0, bitmap),
        Photo(21, "", false, 0, bitmap),
        Photo(4, "", false, 0, bitmap),
        Photo(18, "", false, 0, bitmap)
    )

    val loadCompletion: BehaviorSubject<List<Photo>> = BehaviorSubject.create()

    //    val rawStream: Single<List<Photo>> = Single.just(rawPhotos)
    //    val rawStream: Single<List<Photo>> = Single.fromObservable(loadCompletion.hide())
    val rawStream: Single<List<Photo>> = loadCompletion.hide().first(emptyList())


    val metaData = listOf(
        Photo(10, "", true, 10, null),
        Photo(21, "", false, 3, null),
        Photo(4, "", true, 4, null),
        Photo(18, "", false, 1, null)
    )

    val metaDataEmpty: List<Photo> = emptyList()


    fun getMetaDataById(id: Int, metaRepo: List<Photo>): Maybe<Photo> =
        Maybe.fromCallable {
            metaRepo.find { p ->
                p.id == id
            }
        }

    fun photoWithMetaDataObservable(metaRepo: List<Photo>): Observable<List<Photo>> {
        return rawStream
            .flatMapObservable { list ->
                Observable.fromIterable(list)
            }
            .flatMapMaybe { rawPhoto ->

                getMetaDataById(rawPhoto.id, metaRepo)
                    .doOnError {
                        println("getMetaDataById::doOnError - ${it.message}")
                    }
                    .doOnSuccess {
                        println("getMetaDataById::doOnSuccess [Photo id = ${it.id}]")
                    }
                    .defaultIfEmpty(rawPhoto)
                    .map { photoMetaData ->
                        photoMetaData.copy(bitmap = rawPhoto.bitmap)
                    }
            }
            .doOnComplete {
                println("flatMapMaybe::doOnComplete")
            }
            .toList()
            .doOnSuccess {
                println("toList()::doOnSuccess [list size = ${it.size}]")
            }
            .toObservable()
    }

    @Before
    fun initSubject() {
        loadCompletion.onNext(rawPhotos)
    }

    @Test
    fun shouldReturnPhotoWithMetadataObservable() {
        val obs = photoWithMetaDataObservable(metaData)

        obs.subscribe {
            assert(it.size == rawPhotos.size)

            it.forEach {
                assert(it.bitmap != null)
            }
        }
    }

    @Test
    fun shouldReturnEqualObservables() {
        Observable.sequenceEqual(
            photoWithMetaDataObservable(metaDataEmpty),
            photoWithMetaDataObservable(metaDataEmpty)
        ).subscribe { onNext->
            assert(onNext)
        }
    }
}