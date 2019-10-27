package k.s.yarlykov.libsportfolio.application

import android.app.Application
import k.s.yarlykov.libsportfolio.R
import k.s.yarlykov.libsportfolio.repository.LocalStorage
import k.s.yarlykov.libsportfolio.repository.PhotoRepository

class PortfolioApp : Application(), IRepositoryHelper {

    private lateinit var photoRepository: PhotoRepository

    override fun onCreate() {
        super.onCreate()
        photoRepository = PhotoRepository(
            LocalStorage(
                applicationContext,
                R.array.month_pics,
                R.drawable.bkg_05_may
            )
        )
    }

    override fun getPhotoRepository(): PhotoRepository = photoRepository
}