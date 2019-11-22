package k.s.yarlykov.libsportfolio.repository.instagram

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import k.s.yarlykov.libsportfolio.data.network.InstagramAuthApi
import k.s.yarlykov.libsportfolio.domain.instagram.InstagramToken
import k.s.yarlykov.libsportfolio.logIt

class InstagramAuthHelper (private val api : InstagramAuthApi) : IInstagramAuthHelper {

    private val appCredentialsHolder = BehaviorSubject.create<Pair<String, String>>()

    private val tokenObservable: Observable<InstagramToken> =
        appCredentialsHolder
            .switchMap { (code, secret) ->
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

                Observable.fromCallable { okHttpResponse.body()!! }
            }

    override fun requestToken(appCode: String, appSecret: String): Observable<InstagramToken> {
        appCredentialsHolder.onNext(Pair(appCode, appSecret))
        logIt("InstagramAuthHelper::appCredentialsHolder value = ${appCredentialsHolder.value}")
        return tokenObservable
    }
}