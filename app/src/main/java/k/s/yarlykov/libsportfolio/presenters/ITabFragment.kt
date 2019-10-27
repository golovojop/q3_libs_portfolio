package k.s.yarlykov.libsportfolio.presenters

import com.arellomobile.mvp.MvpView
import k.s.yarlykov.libsportfolio.model.Photo

interface ITabFragment : MvpView {
    fun updateContent(photos: List<Photo>)

}