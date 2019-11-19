package k.s.yarlykov.libsportfolio.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import io.reactivex.disposables.CompositeDisposable
import k.s.yarlykov.libsportfolio.KEY_BUNDLE
import k.s.yarlykov.libsportfolio.KEY_LAYOUT_ID
import k.s.yarlykov.libsportfolio.R
import k.s.yarlykov.libsportfolio.application.PortfolioApp
import k.s.yarlykov.libsportfolio.di.component.DaggerGalleryFragmentComponent
import k.s.yarlykov.libsportfolio.di.component.DaggerInstagramComponent
import k.s.yarlykov.libsportfolio.di.module.instagram.InstagramModule
import k.s.yarlykov.libsportfolio.logIt
import k.s.yarlykov.libsportfolio.presenters.IInstagramFragment
import k.s.yarlykov.libsportfolio.presenters.InstagramPresenter
import k.s.yarlykov.libsportfolio.ui.GridItemDecoration
import k.s.yarlykov.libsportfolio.ui.InstagramWebClient
import k.s.yarlykov.libsportfolio.ui.adapters.InstagramRvAdapter
import kotlinx.android.synthetic.main.fragment_instagram.*
import javax.inject.Inject

class InstagramFragment : Fragment(), IInstagramFragment {

    companion object {
        fun create(bundle: Bundle?): InstagramFragment {
            return InstagramFragment().apply {
                arguments = Bundle().apply {
                    putBundle(KEY_BUNDLE, bundle)
                }
            }
        }
    }

    @Inject
    lateinit var presenter: InstagramPresenter

    private val disposables = CompositeDisposable()

    private val layerWebView = 0
    private val layerLoading = 1
    private val layerRecyclerView = 2

    private val authRequestUri by lazy(LazyThreadSafetyMode.NONE) {
        getString(R.string.auth_base_uri) +
                "?app_id=${getString(R.string.app_id)}" +
                "&redirect_uri=${getString(R.string.app_redirect_uri)}" +
                "&scope=user_profile,user_media&response_type=code"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutId = arguments?.getBundle(KEY_BUNDLE)!!.getInt(KEY_LAYOUT_ID)
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()

        val component = DaggerInstagramComponent
            .builder()
            .instagramModule(
                InstagramModule(
                    getString(R.string.auth_end_point),
                    getString(R.string.graph_end_point)))
            .addDependency((activity!!.application as PortfolioApp).appComponent)
            .bindFragment(this)
            .build()

        component.inject(this)


        presenter.onViewCreated(getString(R.string.app_secret))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun showAuthWebPage() {

        logIt("InstagramFragment::showAuthWebPage()")

        // Начать авторизацию
        webView.apply {
            isVerticalScrollBarEnabled = false
            isHorizontalScrollBarEnabled = false
            settings.javaScriptEnabled = true
            webViewClient = InstagramWebClient(presenter::onAppCodeReceived)
            loadUrl(authRequestUri)
        }
    }

    private fun initRecycleView() {

        rvPics.apply {
            setHasFixedSize(true)
            addItemDecoration(GridItemDecoration(2))
            itemAnimator = DefaultItemAnimator()
            layoutManager = GridLayoutManager(activity?.applicationContext, 2)
            adapter =
                InstagramRvAdapter(R.layout.layout_instagram_rv_item)
        }
    }

    override fun onFrontProgressBar() {
        animator.displayedChild = layerLoading
    }

    override fun onFrontWebView() {
        animator.displayedChild = layerWebView
    }

    override fun onFrontRecyclerView() {
        animator.displayedChild = layerRecyclerView
    }

    override fun updateMediaContent(uri: List<String>) {
        (rvPics.adapter as InstagramRvAdapter).updateModel(uri)
    }
}