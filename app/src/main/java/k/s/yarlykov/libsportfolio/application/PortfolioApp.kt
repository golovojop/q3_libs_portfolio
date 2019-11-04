package k.s.yarlykov.libsportfolio.application

import android.app.Application
import k.s.yarlykov.libsportfolio.R
import k.s.yarlykov.libsportfolio.repository.localstorage.LocalStorage
import k.s.yarlykov.libsportfolio.repository.PhotoRepository

class PortfolioApp : Application(), IRepositoryHelper {

    private lateinit var photoRepository: PhotoRepository

    override fun onCreate() {
        super.onCreate()

        LocalStorage(
            applicationContext,
            R.array.month_pics,
            R.drawable.bkg_05_may
        ).apply {
            doUpload()
            photoRepository = PhotoRepository(this)
        }
    }

    override fun getPhotoRepository(): PhotoRepository = photoRepository
}