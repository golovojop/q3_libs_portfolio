package k.s.yarlykov.libsportfolio.di.module.app

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import k.s.yarlykov.libsportfolio.R
import k.s.yarlykov.libsportfolio.repository.PhotoRepository
import k.s.yarlykov.libsportfolio.repository.localstorage.LocalStorage
import k.s.yarlykov.libsportfolio.repository.room.RoomRepo
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
    fun provideLocalStorage(
        context: Context
    ): LocalStorage = LocalStorage(context, R.array.month_pics, R.drawable.bkg_05_may)

    @Provides
    @Singleton
    fun providePhotoRepository(localStorage: LocalStorage, roomRepo: RoomRepo): PhotoRepository =
        PhotoRepository(localStorage, roomRepo)
}