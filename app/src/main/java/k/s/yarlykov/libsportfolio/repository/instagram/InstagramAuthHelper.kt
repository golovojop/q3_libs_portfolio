package k.s.yarlykov.libsportfolio.repository.instagram

import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import k.s.yarlykov.libsportfolio.data.network.InstagramAuthApi
import k.s.yarlykov.libsportfolio.domain.instagram.InstagramToken
import k.s.yarlykov.libsportfolio.logIt

class InstagramAuthHelper(private val api: InstagramAuthApi) : IInstagramAuthHelper {

    private val appCredentialsHolder = BehaviorSubject.create<Pair<String, String>>()

    private val tokenObservable: Single<InstagramToken> =
        appCredentialsHolder
            .elementAtOrError(0)
            .flatMap { (code, secret) ->
                logIt("InstagramAuthHelper. code:secret ok")
                api.tokenRequest(
                    appId = "937802139939708",
                    appSecret = secret,
                    redirectUri = "https://www.instagram.com/",
                    appCode = code
                )
            }
            .flatMap { okHttpResponse ->
                if (!okHttpResponse.isSuccessful) {
                    throw Throwable("Cant' receive security token")
                }
                Single.fromCallable { okHttpResponse.body()!! }
            }


    override fun requestToken(appCode: String, appSecret: String): Single<InstagramToken> {
        appCredentialsHolder.onNext(Pair(appCode, appSecret))
        return tokenObservable
    }
}