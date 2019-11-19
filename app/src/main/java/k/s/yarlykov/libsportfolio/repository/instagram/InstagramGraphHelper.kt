package k.s.yarlykov.libsportfolio.repository.instagram

import io.reactivex.Observable
import k.s.yarlykov.libsportfolio.data.network.InstagramGraphApi
import k.s.yarlykov.libsportfolio.domain.instagram.MediaNode

class InstagramGraphHelper(private val api : InstagramGraphApi) : IInstagramGraphHelper {

    override fun requestMediaEdge(token: String): Observable<MediaNode> {

        return api.queryUserMediaEdge(token)
            .switchMap { okHttpResponse ->
                if (!okHttpResponse.isSuccessful) {
                    throw Throwable("Cant' receive Media Edge node")
                }
                Observable.fromCallable {okHttpResponse.body()!!}
            }
    }

    override fun requestMediaData(nodeId: String, token: String): Observable<String> {

        return api.requestAlbumContents(nodeId, token)
            .flatMap { okHttpResponse ->
                Observable.fromIterable(
                    if (okHttpResponse.isSuccessful) {
                        okHttpResponse.body()!!.data.map { mediaFile -> mediaFile.mediaUrl }
                    } else {
                        emptyList()
                    }
                )
            }
    }
}