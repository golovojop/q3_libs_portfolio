package k.s.yarlykov.libsportfolio.presenters

import com.arellomobile.mvp.MvpView

interface IInstagramFragment : MvpView {

    fun showLoading()
    fun showContent()

}