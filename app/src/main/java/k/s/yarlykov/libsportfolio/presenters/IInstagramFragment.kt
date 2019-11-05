package k.s.yarlykov.libsportfolio.presenters

import com.arellomobile.mvp.MvpView

interface IInstagramFragment : MvpView {

    fun showProgressBar()
    fun showAuthPage()
    fun showWebView()
    fun loadMediaContent(uri : String)
}