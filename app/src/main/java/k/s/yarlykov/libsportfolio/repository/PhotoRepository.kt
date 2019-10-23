package k.s.yarlykov.libsportfolio.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import k.s.yarlykov.libsportfolio.contracts.RepositoryContract
import k.s.yarlykov.libsportfolio.model.Photo
import kotlin.random.Random

class PhotoRepository(private val context: Context, private val arrayId: Int, private val defaultDrawableId: Int)
    : RepositoryContract.IPhotoRepository {

    companion object {
        private val CAPACITY = 128
        private var memoryCache: HashMap<Int, Photo> = HashMap(CAPACITY)
        private val loadCompletion: BehaviorSubject<Photo> = BehaviorSubject.create()
        private fun addLikes() = Random.nextInt(0, 5)
        private fun favouriteStatus() = Random.nextBoolean()
    }

    override fun getGallery(): Observable<Photo> {
        return Observable.just(Photo(0, 0, false))
    }

    override fun getFavourites(): Observable<Photo> {
        return Observable.just(Photo(0, 0, false))
    }

    private fun addItem(resource: Pair<Int, Bitmap>) {
        memoryCache.put(resource.first, resource.second)
    }

    fun doUpload() {
        createPhotoObservable().subscribe(bitmapObserver)
    }

    private fun createPhotoObservable(): Observable<Photo> {
        return createBitmapObservable()
            .map {(id, bitmap) ->
                Photo(id, bitmap, addLikes(), favouriteStatus())
            }
    }

    private fun createBitmapObservable(): Observable<Pair<Int, Bitmap>> {

        // Уменьшаем размер Bitmap через параметр options.inSampleSize
        val options = BitmapFactory.Options()
        options.inSampleSize = 3

        return createBitmapIdObservable()
            .subscribeOn(Schedulers.io())
            .map { drawableId ->
                Pair(drawableId, BitmapFactory.decodeResource(context.resources, drawableId, options))
            }
    }

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

    private fun getBitmap(id: Int): Bitmap? {
        return memoryCache[id]
    }

    private val bitmapObserver = object : Observer<Photo> {
        lateinit var disposable: Disposable

        override fun onComplete() {
            if(!disposable.isDisposed) disposable.dispose()
            loadCompletion.onNext(Pair(memoryCache.keys.toList(), ::getBitmap))
        }

        override fun onSubscribe(d: Disposable) {
            disposable = d
        }

        override fun onNext(pair: Pair<Int, Bitmap>) {
            addItem(pair)
        }

        override fun onError(e: Throwable) {
            logIt(e.stackTrace.toString())
        }
    }
}