package k.s.yarlykov.libsportfolio.instagram

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import k.s.yarlykov.libsportfolio.R
import kotlinx.android.synthetic.main.activity_instagram.*

class InstagramActivity : AppCompatActivity() {

    val AUTHURL = "https://api.instagram.com/oauth/authorize/"
    val TOKENURL = "https://api.instagram.com/oauth/access_token"
    val APIURL = "https://api.instagram.com/v1"
    val CALLBACKURL = "https://www.instagram.com/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instagram)

        val authUrl = AUTHURL +
                "?app_id=${getString(R.string.app_id)}" +
                "&redirect_uri=${getString(R.string.app_redirect_uri)}" +
                "&scope=user_profile,user_media&response_type=code"

        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.settings.javaScriptEnabled= true
        webView.webViewClient = AuthWebViewClient()
        webView.loadUrl(authUrl)
    }
}