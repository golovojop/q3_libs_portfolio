package k.s.yarlykov.libsportfolio.application

import android.app.Application
import k.s.yarlykov.libsportfolio.R
import k.s.yarlykov.libsportfolio.di.component.app.AppComponent
import k.s.yarlykov.libsportfolio.di.component.app.DaggerAppComponent
import k.s.yarlykov.libsportfolio.di.module.app.AppModule
import k.s.yarlykov.libsportfolio.di.module.app.NetworkModule
import k.s.yarlykov.libsportfolio.repository.localstorage.LocalStorage
import k.s.yarlykov.libsportfolio.repository.PhotoRepository

class PortfolioApp : Application(), IRepositoryHelper {

    private lateinit var photoRepository: PhotoRepository

    val appModule: AppModule by lazy(LazyThreadSafetyMode.NONE) {
        AppModule(this)
    }

    val appComponent: AppComponent by lazy(LazyThreadSafetyMode.NONE) {

        DaggerAppComponent
            .builder()
            .appModule(appModule)
            .networkModule(netModule)
            .build()
    }

    val netModule: NetworkModule by lazy(LazyThreadSafetyMode.NONE) {
        NetworkModule()
    }

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