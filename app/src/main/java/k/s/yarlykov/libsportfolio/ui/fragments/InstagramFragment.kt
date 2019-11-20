package k.s.yarlykov.libsportfolio.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import k.s.yarlykov.libsportfolio.KEY_BUNDLE
import k.s.yarlykov.libsportfolio.KEY_LAYOUT_ID
import k.s.yarlykov.libsportfolio.R
import k.s.yarlykov.libsportfolio.di.component.ui.DaggerInstagramComponent
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

    private val layerWebView = 0
    private val layerLoading = 1
    private val layerRecyclerView = 2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutId = arguments?.getBundle(KEY_BUNDLE)!!.getInt(KEY_LAYOUT_ID)
        return inflater.inflate(layoutId, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val parent = context as ParentDependencies

        val component = DaggerInstagramComponent
            .builder()
            .instagramModule(
                InstagramModule(
                    getString(R.string.auth_end_point),
                    getString(R.string.graph_end_point)))
            .addDependency(parent.getComponent())
            .bindFragment(this)
            .build()

        component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
        presenter.onViewCreated(getString(R.string.app_secret))
    }

    override fun showAuthWebPage(authRequestUri : String) {

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