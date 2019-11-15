package k.s.yarlykov.libsportfolio.di.module.app

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import k.s.yarlykov.libsportfolio.R
import k.s.yarlykov.libsportfolio.repository.PhotoRepository
import k.s.yarlykov.libsportfolio.repository.localstorage.LocalStorage
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context = application

    @Provides
    @Singleton
    fun provideApplication(): Application = application

//    @Provides
//    @Singleton
//    @Named("array_id")
//    val arrayId : Int
//    get() = R.array.month_pics

    @Provides
    @Singleton
    fun provideLocalStorage(
        context: Context,
        picsArrayId: Int = R.array.month_pics,
        picDefaultId: Int = R.drawable.bkg_05_may
    ): LocalStorage = LocalStorage(context, picsArrayId, picDefaultId)

    @Provides
    @Singleton
    fun providePhotoRepository(localStorage : LocalStorage) : PhotoRepository =
        PhotoRepository(localStorage)
}