package k.s.yarlykov.libsportfolio.instagram.network

import io.reactivex.Observable
import k.s.yarlykov.libsportfolio.model.instagram.InstagramToken
import k.s.yarlykov.libsportfolio.model.instagram.MediaFile
import k.s.yarlykov.libsportfolio.model.instagram.MediaFileList
import k.s.yarlykov.libsportfolio.model.instagram.MediaNode
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface InstagramGraphApi {

    @GET("me/media?fields=id,caption,media_url,permalink")
    fun queryUserMediaEdge( @Query("access_token") token : String) : Observable<Response<MediaNode>>

    @GET("{media_id}/children?fields=media_url,permalink")
    fun requestAlbumContents(@Path("media_id") mediaId : String, @Query("access_token") token : String)
            : Observable<Response<MediaFileList>>
}