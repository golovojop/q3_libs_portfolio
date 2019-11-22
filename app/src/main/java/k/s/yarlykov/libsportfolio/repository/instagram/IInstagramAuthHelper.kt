package k.s.yarlykov.libsportfolio.repository.instagram

import io.reactivex.Single
import k.s.yarlykov.libsportfolio.domain.instagram.InstagramToken

interface IInstagramAuthHelper {
    fun requestToken(appCode: String, appSecret: String) : Single<InstagramToken>
}