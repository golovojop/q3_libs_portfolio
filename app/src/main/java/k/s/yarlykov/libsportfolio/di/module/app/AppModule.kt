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

    @Provides
    @Singleton
    @Named("pics_array_id")
    fun providePicsArrayId() : Int = R.array.month_pics

    @Provides
    @Singleton
    @Named("pic_default_id")
    fun providePicDefaultId() : Int = R.drawable.bkg_05_may

    @Provides
    @Singleton
    fun provideLocalStorage(
        context: Context,
        @Named("pics_array_id") picsArrayId: Int,
        @Named("pic_default_id") picDefaultId: Int
    ): LocalStorage = LocalStorage(context, picsArrayId, picDefaultId)

    @Provides
    @Singleton
    fun providePhotoRepository(localStorage: LocalStorage): PhotoRepository =
        PhotoRepository(localStorage)
}