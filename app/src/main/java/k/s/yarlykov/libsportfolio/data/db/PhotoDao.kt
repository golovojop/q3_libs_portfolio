package k.s.yarlykov.libsportfolio.data.db

import androidx.room.*
import k.s.yarlykov.libsportfolio.domain.room.Photo

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(ps: Iterable<Photo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(p: Photo)

    @Update
    fun update(p: Photo)

    @Delete
    fun delete(p: Photo)

    @Query("select * from photo")
    fun select(): List<Photo>

    @Query("delete from photo")
    fun clear()
}

