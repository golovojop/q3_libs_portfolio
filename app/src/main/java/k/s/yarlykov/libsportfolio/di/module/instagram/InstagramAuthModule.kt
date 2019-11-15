package k.s.yarlykov.libsportfolio.di.module.instagram

import dagger.Module
import dagger.Provides
import k.s.yarlykov.libsportfolio.instagram.network.InstagramAuthApi
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class InstagramAuthModule (baseUrl: String) : InstagramBaseModule(baseUrl) {

    @Provides
    @Singleton
    fun provideInstagramAuthApi(retrofit: Retrofit): InstagramAuthApi =
        retrofit.create(InstagramAuthApi::class.java)

}