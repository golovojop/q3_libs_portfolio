package k.s.yarlykov.libsportfolio.application

import k.s.yarlykov.libsportfolio.repository.PhotoRepository

interface IRepositoryHelper {
    fun getPhotoRepository() : PhotoRepository
}