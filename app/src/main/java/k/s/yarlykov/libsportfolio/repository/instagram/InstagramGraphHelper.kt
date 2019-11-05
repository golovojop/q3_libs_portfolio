package k.s.yarlykov.libsportfolio.repository.instagram

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import k.s.yarlykov.libsportfolio.instagram.network.InstagramGraphApi
import k.s.yarlykov.libsportfolio.model.instagram.MediaNode
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object InstagramGraphHelper : IInstagramGraphHelper {
    private const val baseUrl = "https://graph.instagram.com/"
    private const val HTTP_OK = 200

    private val api = initApiAdapter()

    override fun requestMediaEdge(token: String): Observable<MediaNode> {

        return api.queryUserMediaEdge(token)
            .switchMap { okHttpResponse ->
                Observable.fromCallable {
                    if (okHttpResponse.code() == HTTP_OK) {
                        okHttpResponse.body()!!
                    } else {

                        /**
                         * Temporary
                         */
                        MediaNode()
                    }
                }
            }
    }

    fun requestMediaData(nodeId: String, token: String): Observable<String> {

        return api.requestAlbumContents(nodeId, token)
            .flatMap { okHttpResponse ->
                Observable.fromIterable(
                    if (okHttpResponse.code() == HTTP_OK) {
                        okHttpResponse.body()!!.data.map { mediaFile -> mediaFile.mediaUrl }
                    } else {
                        emptyList()
                    }
                )
            }
    }

    private fun initApiAdapter(): InstagramGraphApi {
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

        return adapter.create(InstagramGraphApi::class.java)
    }
}