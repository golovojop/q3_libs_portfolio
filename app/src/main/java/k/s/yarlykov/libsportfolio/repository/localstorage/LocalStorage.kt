package k.s.yarlykov.libsportfolio.repository.localstorage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import k.s.yarlykov.libsportfolio.domain.room.Photo
import kotlin.random.Random

class LocalStorage(
    private val context: Context,
    private val arrayId: Int,
    private val defaultDrawableId: Int
) : ILocalStorage {

    override fun connectToBitmapStream(): Single<List<Photo>> =
        loadCompletion.hide().elementAtOrError(0)

    override fun addPhoto(photo: Photo) {
        memoryCache[photo.id] = photo
        update()
    }

    override fun deletePhoto(photo: Photo) {
        memoryCache.remove(photo.id)
        update()
    }

    override fun populateCache() {
        createPhotoObservable()
            .subscribeOn(Schedulers.io())
            .subscribe(photoObserver)
    }

    private fun update() {
        loadCompletion.onNext(memoryCache.values.toList())
    }

    // Step 3
    private fun createPhotoObservable(): Observable<Photo> {
        return createBitmapObservable()
            .map { (id, bitmap) ->
                Photo(
                    id,         // ID ресурса картинки использовать как ID в базе
                    "",
                    setFavouriteStatus(),
                    addLikes(),
                    bitmap
                )
            }
    }

    // Step 2
    private fun createBitmapObservable(): Observable<Pair<Int, Bitmap>> {

        // Уменьшить размер Bitmap через параметр options.inSampleSize
        val options = BitmapFactory.Options()
        options.inSampleSize = 3

        return createBitmapIdObservable()
            .subscribeOn(Schedulers.io())
            .map { drawableId ->
                Pair(
                    drawableId,
                    BitmapFactory.decodeResource(context.resources, drawableId, options)
                )
            }
    }

    // Step 1
    private fun createBitmapIdObservable(): Observable<Int> {
        return Observable.create { emitter ->
            try {
                with(context.resources.obtainTypedArray(arrayId)) {
                    (0 until length()).forEach { i ->
                        emitter.onNext(getResourceId(i, defaultDrawableId))
                    }
                    recycle()
                    emitter.onComplete()
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    /**
     * @onNext - записывает в кеш очередную Photo
     * @onComplete - собирает все Photo в список и пушит в BehaviorSubject
     */
    private val photoObserver = object : Observer<Photo> {
        override fun onSubscribe(d: Disposable) {
        }

        override fun onNext(photo: Photo) {
            memoryCache[photo.id] = photo
        }

        override fun onComplete() {
            loadCompletion.onNext(memoryCache.values.toList())
        }

        override fun onError(e: Throwable) {
        }
    }

    companion object {
        // Принимает и отдает в потоке Schedulers.io()
        private val loadCompletion: BehaviorSubject<List<Photo>> = BehaviorSubject.create()

        private const val CAPACITY = 128
        private var memoryCache: HashMap<Int, Photo> = HashMap(CAPACITY)
        private fun addLikes() = 0//Random.nextInt(0, 5)
        private fun setFavouriteStatus() = false//Random.nextBoolean()
    }
}