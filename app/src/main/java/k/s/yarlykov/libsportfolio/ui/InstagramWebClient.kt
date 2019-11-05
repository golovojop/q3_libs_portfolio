package k.s.yarlykov.libsportfolio.ui

import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import k.s.yarlykov.libsportfolio.logIt

class InstagramWebClient(val callback : (String) -> Unit) : WebViewClient() {

    init {
        logIt("InstagramWebClient created")
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        logIt("onPageFinished:: url = $url")

        url?.let {
            if(it.contains("?code=")) {
                val uri = Uri.parse(it)
                uri.getQueryParameter("code")?.let{code ->
                    callback(code)
                }
            }
        }
    }
}