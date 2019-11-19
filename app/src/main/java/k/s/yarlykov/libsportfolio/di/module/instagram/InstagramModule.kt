package k.s.yarlykov.libsportfolio.di.module.instagram

import android.app.Application
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import k.s.yarlykov.libsportfolio.di.scope.InstagramScope
import k.s.yarlykov.libsportfolio.instagram.network.InstagramAuthApi
import k.s.yarlykov.libsportfolio.instagram.network.InstagramGraphApi
import k.s.yarlykov.libsportfolio.presenters.InstagramPresenter
import k.s.yarlykov.libsportfolio.repository.instagram.IInstagramAuthHelper
import k.s.yarlykov.libsportfolio.repository.instagram.IInstagramGraphHelper
import k.s.yarlykov.libsportfolio.repository.instagram.InstagramAuthHelper
import k.s.yarlykov.libsportfolio.repository.instagram.InstagramGraphHelper
import k.s.yarlykov.libsportfolio.ui.fragments.InstagramFragment
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
open class InstagramModule(private val authUri: String, private val graphUri: String) {

    @Provides
    @InstagramScope
    fun provideOkHttpCache(application: Application): Cache =
        Cache(application.cacheDir, (10 * 1024).toLong())

    @Provides
    @InstagramScope
    fun provideGson(): Gson =
        GsonBuilder().apply {
            setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        }.create()

    @Provides
    @InstagramScope
    fun provideOkHttpClient(cache: Cache): OkHttpClient =
        OkHttpClient()
            .newBuilder()
            .cache(cache)
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

    @Provides
    @InstagramScope
    fun provideCallAdapterFactory(): CallAdapter.Factory =
        RxJava2CallAdapterFactory.create()

    @Provides
    @InstagramScope
    @Named("auth_retrofit")
    fun provideAuthRetrofit(
        gson: Gson,
        factory: CallAdapter.Factory,
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(factory)
            .baseUrl(authUri)
            .client(okHttpClient)
            .build()

    @Provides
    @InstagramScope
    @Named("graph_retrofit")
    fun provideGraphRetrofit(
        gson: Gson,
        factory: CallAdapter.Factory,
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(factory)
            .baseUrl(graphUri)
            .client(okHttpClient)
            .build()

    @Provides
    @InstagramScope
    fun provideInstagramAuthApi(@Named("auth_retrofit") retrofit: Retrofit): InstagramAuthApi =
        retrofit.create(InstagramAuthApi::class.java)

    @Provides
    @InstagramScope
    fun provideInstagramGraphApi(@Named("graph_retrofit") retrofit: Retrofit): InstagramGraphApi =
        retrofit.create(InstagramGraphApi::class.java)


    @Provides
    @InstagramScope
    fun provideInstagramAuthHelper(api: InstagramAuthApi): IInstagramAuthHelper =
        InstagramAuthHelper(api)

    @Provides
    @InstagramScope
    fun provideInstagramGraphHelper(api: InstagramGraphApi): IInstagramGraphHelper =
        InstagramGraphHelper(api)

    @Provides
    @InstagramScope
    fun provideInstagramPresenter(
        fragment: InstagramFragment,
        authHelper: IInstagramAuthHelper,
        graphHelper: IInstagramGraphHelper
    ): InstagramPresenter {
        return InstagramPresenter(fragment, authHelper, graphHelper)
    }
}