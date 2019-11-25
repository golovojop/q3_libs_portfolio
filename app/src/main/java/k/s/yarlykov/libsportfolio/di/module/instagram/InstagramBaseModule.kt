package k.s.yarlykov.libsportfolio.di.module.instagram

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
open class InstagramBaseModule (private val baseUrl: String) {

    @Provides
    @Singleton
    fun provideRetrofit(
        gson: Gson,
        factory: CallAdapter.Factory,
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(factory)
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
}