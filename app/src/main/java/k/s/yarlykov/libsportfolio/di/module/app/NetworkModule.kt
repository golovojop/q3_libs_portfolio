package k.s.yarlykov.libsportfolio.di.module.app

import android.app.Application
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import java.util.concurrent.TimeUnit

import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpCache(application: Application): Cache =
        Cache(application.cacheDir, (10 * 1024).toLong())

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder().apply {
            setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        }.create()

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache): OkHttpClient =
        OkHttpClient()
            .newBuilder()
            .cache(cache)
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideCallAdapterFactory(): CallAdapter.Factory =
        RxJava2CallAdapterFactory.create()
}
