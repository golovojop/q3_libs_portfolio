package k.s.yarlykov.libsportfolio.application

import android.app.Application
import k.s.yarlykov.libsportfolio.R
import k.s.yarlykov.libsportfolio.di.component.app.AppComponent
import k.s.yarlykov.libsportfolio.di.component.app.DaggerAppComponent
import k.s.yarlykov.libsportfolio.di.module.app.AppModule
import k.s.yarlykov.libsportfolio.di.module.app.NetworkModule
import k.s.yarlykov.libsportfolio.di.module.orm.OrmRoomModule

class PortfolioApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule())
            .ormRoomModule(OrmRoomModule(getString(R.string.db_name)))
            .build()
    }
}