package k.s.yarlykov.libsportfolio.repository.instagram

import io.reactivex.Observable
import k.s.yarlykov.libsportfolio.model.instagram.InstagramToken

interface IInstagramRepo {

    fun requestToken(appCode: String, appSecret: String) : Observable<InstagramToken>
}