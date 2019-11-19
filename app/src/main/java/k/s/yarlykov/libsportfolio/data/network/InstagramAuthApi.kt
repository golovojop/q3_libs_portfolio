package k.s.yarlykov.libsportfolio.data.network

import io.reactivex.Observable
import k.s.yarlykov.libsportfolio.domain.instagram.InstagramToken
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface InstagramAuthApi {

    @FormUrlEncoded
    @POST("oauth/access_token")
    fun tokenRequest(@Field("app_id") appId : String,
                     @Field("app_secret") appSecret : String,
                     @Field("code") appCode : String,
                     @Field("redirect_uri") redirectUri : String,
                     @Field("grant_type") grantType : String = "authorization_code")
            : Observable<Response<InstagramToken>>

}