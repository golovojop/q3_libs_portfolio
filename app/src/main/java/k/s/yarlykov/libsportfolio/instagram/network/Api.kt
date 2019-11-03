package k.s.yarlykov.libsportfolio.instagram.network

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {

    @FormUrlEncoded
    @POST("oauth/access_token")
    fun tokenRequest(@Field("app_id") appId : String,
                     @Field("app_secret") appSecret : String,
                     @Field("code") appCode : String,
                     @Field("redirect_uri") redirectUri : String,
                     @Field("grant_type") grantType : String = "authorization_code")
            : Observable<Response<InstagramToken>>

}