package k.s.yarlykov.libsportfolio.instagram

import android.net.Uri
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import k.s.yarlykov.libsportfolio.instagram.repo.InstagramRepo

class AuthWebViewClient : WebViewClient() {

    var count = 0
    private set

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
//        Log.d("APP_TAG", "${++count}: $url")

        url?.let {
            if(it.contains("?code=")) {
                val uri = Uri.parse(it)
                val code = uri.getQueryParameter("code")
                Log.d("APP_TAG", code)

                InstagramRepo
                    .requestToken(Pair(code, "61a0e40678f3f39d64133afb848f0fb1"))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Log.d("APP_TAG", "token: ${it.accessToken}")
                    }
            }
        }
    }
}