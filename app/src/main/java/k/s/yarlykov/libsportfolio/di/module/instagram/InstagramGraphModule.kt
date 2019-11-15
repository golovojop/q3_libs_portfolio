package k.s.yarlykov.libsportfolio.di.module.instagram

import dagger.Module
import dagger.Provides
import k.s.yarlykov.libsportfolio.instagram.network.InstagramGraphApi
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class InstagramGraphModule(baseUrl: String) : InstagramBaseModule(baseUrl) {

    @Provides
    @Singleton
    fun provideInstagramGraphApi(retrofit: Retrofit): InstagramGraphApi =
        retrofit.create(InstagramGraphApi::class.java)

}

