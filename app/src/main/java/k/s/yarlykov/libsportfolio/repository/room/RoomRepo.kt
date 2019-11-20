package k.s.yarlykov.libsportfolio.repository.room

import io.reactivex.Maybe
import io.reactivex.Single
import k.s.yarlykov.libsportfolio.data.db.PhotoDao
import k.s.yarlykov.libsportfolio.domain.room.Photo

class RoomRepo(private val photoDao: PhotoDao) : IRoomRepo {

    override fun getPhoto(): Maybe<List<Photo>> =
        photoDao.select()

    override fun getPhoto(id: Int): Maybe<Photo> =
        photoDao.select(id)

    override fun insertPhoto(photo: Photo) =
        photoDao.insert(photo)
}