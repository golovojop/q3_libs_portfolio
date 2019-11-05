package k.s.yarlykov.libsportfolio.ui

import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import k.s.yarlykov.libsportfolio.logIt

class InstagramWebClient : WebViewClient() {

    private val appCode = BehaviorSubject.create<String>()

    fun getApplicationCode() : Observable<String> {
        return appCode.hide()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        logIt("onPageFinished:: url = $url")

        url?.let {
            if(it.contains("?code=")) {
                val uri = Uri.parse(it)
                val code = uri.getQueryParameter("code")

                code?.let {c ->
                    if(appCode.value == null) {
                        appCode.onNext(code)
                    } else if (!appCode.value.equals(c, false)) {
                        appCode.onNext(code)
                    }
                }
                logIt("onPageFinished:: code = $code")
            }
        }
    }
}