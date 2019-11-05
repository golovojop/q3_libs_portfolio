package k.s.yarlykov.libsportfolio.presenters

import com.arellomobile.mvp.MvpView

interface IInstagramFragment : MvpView {

    fun showProgressBar()
    fun showWebView()
    fun showRecyclerView()

    fun showAuthPage()
    fun loadMediaContent(uri : String)
    fun updateMediaContent(uri : List<String>)
}