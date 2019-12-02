package k.s.yarlykov.libsportfolio.data.db

import androidx.room.*
import io.reactivex.Maybe
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
    fun select(): Maybe<List<Photo>>

    /**
     * Запись есть - onSuccess
     * Записи нет - onComplete
     */
    @Query("select * from photo where id = :id")
    fun select(id: Int): Maybe<Photo>

    @Query("delete from photo")
    fun clear()
}

