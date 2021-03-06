package k.s.yarlykov.libsportfolio.di.component.app

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import dagger.Component
import k.s.yarlykov.libsportfolio.di.module.app.AppModule
import k.s.yarlykov.libsportfolio.di.module.app.NetworkModule
import k.s.yarlykov.libsportfolio.di.module.orm.OrmRoomModule
import k.s.yarlykov.libsportfolio.repository.PhotoRepository
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import javax.inject.Singleton

@Singleton
@Component(modules=[AppModule::class, NetworkModule::class, OrmRoomModule::class])

interface AppComponent {
    fun getContext() : Context
    fun getApplication() : Application

    fun getOkhttp3Cache() : okhttp3.Cache
    fun getGson() : Gson
    fun getOkHttpClient() : OkHttpClient
    fun getCallAdapterFactory() : CallAdapter.Factory

    fun getPhotoRepository() : PhotoRepository
}