package k.s.yarlykov.libsportfolio.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import k.s.yarlykov.libsportfolio.domain.room.Photo

@Database(entities = [Photo::class], version = 1, exportSchema = false)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun photoDao() : PhotoDao
}