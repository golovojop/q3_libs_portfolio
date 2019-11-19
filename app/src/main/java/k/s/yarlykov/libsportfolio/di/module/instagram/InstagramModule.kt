package k.s.yarlykov.libsportfolio.di.module.instagram

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import k.s.yarlykov.libsportfolio.R
import k.s.yarlykov.libsportfolio.di.scope.InstagramScope
import k.s.yarlykov.libsportfolio.instagram.network.InstagramAuthApi
import k.s.yarlykov.libsportfolio.instagram.network.InstagramGraphApi
import k.s.yarlykov.libsportfolio.presenters.InstagramPresenter
import k.s.yarlykov.libsportfolio.repository.instagram.IInstagramAuthHelper
import k.s.yarlykov.libsportfolio.repository.instagram.IInstagramGraphHelper
import k.s.yarlykov.libsportfolio.repository.instagram.InstagramAuthHelper
import k.s.yarlykov.libsportfolio.repository.instagram.InstagramGraphHelper
import k.s.yarlykov.libsportfolio.ui.fragments.InstagramFragment
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
class InstagramModule(private val authUri: String, private val graphUri: String) {

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
    fun provideAuthRequestUri(context: Context): String =
        context.getString(R.string.auth_base_uri) +
                "?app_id=${context.getString(R.string.app_id)}" +
                "&redirect_uri=${context.getString(R.string.app_redirect_uri)}" +
                "&scope=user_profile,user_media&response_type=code"

    @Provides
    @InstagramScope
    fun provideInstagramPresenter(
        fragment: InstagramFragment,
        authHelper: IInstagramAuthHelper,
        graphHelper: IInstagramGraphHelper,
        authRequestUri: String
    ): InstagramPresenter {
        return InstagramPresenter(fragment, authHelper, graphHelper, authRequestUri)
    }
}