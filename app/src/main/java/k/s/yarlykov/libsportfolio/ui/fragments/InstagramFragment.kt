package k.s.yarlykov.libsportfolio.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import io.reactivex.disposables.CompositeDisposable
import k.s.yarlykov.libsportfolio.KEY_BUNDLE
import k.s.yarlykov.libsportfolio.KEY_LAYOUT_ID
import k.s.yarlykov.libsportfolio.R
import k.s.yarlykov.libsportfolio.instagram.InstagramWebClient
import k.s.yarlykov.libsportfolio.presenters.IInstagramFragment
import k.s.yarlykov.libsportfolio.presenters.InstagramPresenter
import kotlinx.android.synthetic.main.fragment_instagram.*

class InstagramFragment : MvpAppCompatFragment(), IInstagramFragment {

    companion object {
        fun create(bundle: Bundle?): InstagramFragment {
            return InstagramFragment().apply {
                arguments = Bundle().apply {
                    putBundle(KEY_BUNDLE, bundle)
                }
            }
        }
    }

    @InjectPresenter
    lateinit var presenter: InstagramPresenter

    lateinit var authRequestUri: String
    lateinit var webClient: InstagramWebClient

    private val disposables = CompositeDisposable()

    private val layerContent = 0
    private val layerLoading = 1

    private val authBaseUri = "https://api.instagram.com/oauth/authorize/"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutId = arguments?.getBundle(KEY_BUNDLE)!!.let { bundle ->
            bundle.getInt(KEY_LAYOUT_ID)
        }

        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authRequestUri = authBaseUri +
                "?app_id=${getString(R.string.app_id)}" +
                "&redirect_uri=${getString(R.string.app_redirect_uri)}" +
                "&scope=user_profile,user_media&response_type=code"

        webClient = InstagramWebClient()

        presenter.onViewCreated()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun startLoading() {
        animator.displayedChild = layerLoading

        disposables.add(webClient
            .getApplicationCode()
            .subscribe { code ->
                presenter.onAppCodeReceived(code, getString(R.string.app_secret))
            })

        webView.apply {
            isVerticalScrollBarEnabled = false
            isHorizontalScrollBarEnabled = false
            settings.javaScriptEnabled = true
            webViewClient = webClient
            loadUrl(authRequestUri)
        }
    }

    override fun showLoading() {
        animator.displayedChild = layerLoading
    }

    override fun showContent() {
        animator.displayedChild = layerContent
    }

    override fun showMedia(uri : String) {
        webView.loadUrl(uri)
    }
}