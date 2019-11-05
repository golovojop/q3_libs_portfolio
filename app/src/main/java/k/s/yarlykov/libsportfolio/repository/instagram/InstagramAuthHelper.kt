package k.s.yarlykov.libsportfolio.repository.instagram

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import k.s.yarlykov.libsportfolio.instagram.network.InstagramAuthApi
import k.s.yarlykov.libsportfolio.model.instagram.InstagramToken
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object InstagramAuthHelper : IInstagramAuthHelper {
    private const val baseUrl = "https://api.instagram.com/"
    private const val HTTP_OK = 200

    private val appCredentialsHolder = BehaviorSubject.create<Pair<String, String>>()
    private val api by lazy { initApiAdapter() }

    private val tokenObservable: Observable<InstagramToken> =
        appCredentialsHolder
            .switchMap { (code, secret) ->
                api.tokenRequest(
                    appId = "937802139939708",
                    appSecret = secret,
                    redirectUri = "https://www.instagram.com/",
                    appCode = code
                )
            }
            .flatMap { okHttpResponse ->
                if (okHttpResponse.code() != HTTP_OK) {
                    throw Throwable("Cant' receive security token")
                }

                Observable.fromCallable { okHttpResponse.body()!! }
            }

    override fun requestToken(appCode: String, appSecret: String): Observable<InstagramToken> {
        appCredentialsHolder.onNext(Pair(appCode, appSecret))
        return tokenObservable
    }

    private fun initApiAdapter(): InstagramAuthApi {
        // Установить таймауты
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

        val adapter = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return adapter.create(InstagramAuthApi::class.java)
    }
}