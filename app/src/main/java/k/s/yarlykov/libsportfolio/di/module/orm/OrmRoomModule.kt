package k.s.yarlykov.libsportfolio.di.module.orm

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import k.s.yarlykov.libsportfolio.data.db.AppRoomDatabase
import k.s.yarlykov.libsportfolio.data.db.PhotoDao
import k.s.yarlykov.libsportfolio.repository.room.RoomRepo
import javax.inject.Singleton

@Module
class OrmRoomModule(private val dbName: String) {

    @Singleton
    @Provides
    fun provideRoomDatabase(context: Context): AppRoomDatabase =
        Room.databaseBuilder(context, AppRoomDatabase::class.java, dbName).build()

    @Singleton
    @Provides
    fun providePhotoDao(db: AppRoomDatabase): PhotoDao =
        db.photoDao()

    @Singleton
    @Provides
    fun provideRoomRepo(photoDao: PhotoDao) : RoomRepo =
        RoomRepo(photoDao)
}