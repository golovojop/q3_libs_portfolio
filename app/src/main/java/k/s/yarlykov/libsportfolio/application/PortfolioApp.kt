package k.s.yarlykov.libsportfolio.application

import android.app.Application
import k.s.yarlykov.libsportfolio.R
import k.s.yarlykov.libsportfolio.di.component.app.AppComponent
import k.s.yarlykov.libsportfolio.di.component.app.DaggerAppComponent
import k.s.yarlykov.libsportfolio.di.module.app.AppModule
import k.s.yarlykov.libsportfolio.di.module.app.NetworkModule
import k.s.yarlykov.libsportfolio.di.module.orm.OrmRoomModule
import k.s.yarlykov.libsportfolio.repository.localstorage.LocalStorage
import k.s.yarlykov.libsportfolio.repository.PhotoRepository

class PortfolioApp : Application()/*, IRepositoryHelper */{

    private val appModule: AppModule by lazy(LazyThreadSafetyMode.NONE) {
        AppModule(this)
    }

    private val netModule: NetworkModule by lazy(LazyThreadSafetyMode.NONE) {
        NetworkModule()
    }

    private val roomModule: OrmRoomModule by lazy(LazyThreadSafetyMode.NONE) {
        OrmRoomModule(getString(R.string.db_name))
    }

    val appComponent: AppComponent by lazy(LazyThreadSafetyMode.NONE) {

        DaggerAppComponent
            .builder()
            .appModule(appModule)
            .networkModule(netModule)
            .ormRoomModule(roomModule)
            .build()
    }
}