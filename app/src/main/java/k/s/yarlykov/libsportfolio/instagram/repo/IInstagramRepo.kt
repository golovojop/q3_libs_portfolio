package k.s.yarlykov.libsportfolio.instagram.repo

import io.reactivex.Observable
import k.s.yarlykov.libsportfolio.instagram.network.InstagramToken

interface IInstagramRepo {

    fun requestToken(data : Pair<String, String>) : Observable<InstagramToken>
}