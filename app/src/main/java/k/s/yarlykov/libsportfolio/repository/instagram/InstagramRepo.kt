package k.s.yarlykov.libsportfolio.repository.instagram

import android.util.Log
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import k.s.yarlykov.libsportfolio.instagram.network.InstagramApi
import k.s.yarlykov.libsportfolio.model.instagram.InstagramToken
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object InstagramRepo : IInstagramRepo {
    private const val baseUrl = "https://api.instagram.com/"
    private const val HTTP_OK = 200

    private val appCredentialsHolder = BehaviorSubject.create<Pair<String, String>>()
    private val api = initApiAdapter()

    private val dataSource: Observable<InstagramToken> =
        appCredentialsHolder
            .switchMap { (code, secret) ->
                Log.d("APP_T", "switchMap:: $code, $secret")

                api.tokenRequest(appId = "937802139939708",
                    appSecret = secret,
                    redirectUri = "https://www.instagram.com/",
                    appCode = code)
            }
            .flatMap {okHttpResponse ->
                Log.d("APP_T", "flatMap:: ${okHttpResponse.code()}")

                Observable.fromCallable {
                    if(okHttpResponse.code() == HTTP_OK) {
                        okHttpResponse.body()!!
                    } else {
                        InstagramToken()
                    }
                }
            }

    override fun requestToken(appCode: String, appSecret: String) : Observable<InstagramToken> {
        appCredentialsHolder.onNext(Pair(appCode, appSecret))
        return dataSource
    }

    private fun initApiAdapter(): InstagramApi {
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

        return adapter.create(InstagramApi::class.java)
    }
}