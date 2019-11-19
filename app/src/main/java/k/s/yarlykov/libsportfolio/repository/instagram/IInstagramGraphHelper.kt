package k.s.yarlykov.libsportfolio.repository.instagram

import io.reactivex.Observable
import k.s.yarlykov.libsportfolio.model.instagram.MediaNode

interface IInstagramGraphHelper {

    fun requestMediaEdge(token: String): Observable<MediaNode>
    fun requestMediaData(nodeId: String, token: String): Observable<String>
}