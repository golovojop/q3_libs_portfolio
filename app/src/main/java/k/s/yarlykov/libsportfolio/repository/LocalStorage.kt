package k.s.yarlykov.libsportfolio.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import k.s.yarlykov.libsportfolio.contracts.StorageContract
import k.s.yarlykov.libsportfolio.model.Photo
import kotlin.random.Random

class LocalStorage(private val context: Context, private val arrayId: Int, private val defaultDrawableId: Int) :
    StorageContract.ILocalStorage {

    override fun connect(): Observable<List<Photo>> {
        return loadCompletion.hide()
    }

    override fun addPhoto(photo: Photo) {
        memoryCache[photo.id] = photo
        update()
    }

    override fun deletePhoto(photo: Photo) {
        memoryCache.remove(photo.id)
        update()
    }

    private fun update() {
        loadCompletion.onNext(memoryCache.values.toList())
    }

    fun doUpload() {
        createPhotoObservable().subscribe(photoObserver)
    }

    // Step 3
    private fun createPhotoObservable(): Observable<Photo> {
        return createBitmapObservable()
            .map { (id, bitmap) ->
                Photo(id, bitmap, addLikes(), setFavouriteStatus())
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
                Pair(drawableId, BitmapFactory.decodeResource(context.resources, drawableId, options))
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

    private val photoObserver = object : Observer<Photo> {
        lateinit var disposable: Disposable

        override fun onSubscribe(d: Disposable) {
            disposable = d
        }

        override fun onNext(photo: Photo) {
            memoryCache[photo.id] = photo
        }

        override fun onComplete() {
            if (!disposable.isDisposed) disposable.dispose()
            loadCompletion.onNext(memoryCache.values.toList())
        }

        override fun onError(e: Throwable) {
        }
    }

    companion object {
        private const val CAPACITY = 128
        private var memoryCache: HashMap<Int, Photo> = HashMap(CAPACITY)
        private val loadCompletion: BehaviorSubject<List<Photo>> = BehaviorSubject.create()
        private fun addLikes() = Random.nextInt(0, 5)
        private fun setFavouriteStatus() = Random.nextBoolean()
    }
}