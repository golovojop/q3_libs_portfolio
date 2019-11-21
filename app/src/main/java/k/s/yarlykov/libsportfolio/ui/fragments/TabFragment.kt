package k.s.yarlykov.libsportfolio.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import k.s.yarlykov.libsportfolio.CONTENT
import k.s.yarlykov.libsportfolio.KEY_BUNDLE
import k.s.yarlykov.libsportfolio.KEY_LAYOUT_ID
import k.s.yarlykov.libsportfolio.di.component.ui.MainActivityComponent
import k.s.yarlykov.libsportfolio.di.module.ui.MainActivityModule
import k.s.yarlykov.libsportfolio.domain.room.Photo
import k.s.yarlykov.libsportfolio.presenters.ITabFragment
import k.s.yarlykov.libsportfolio.presenters.TabPresenter
import k.s.yarlykov.libsportfolio.ui.GridItemDecoration
import k.s.yarlykov.libsportfolio.ui.IDependencies
import k.s.yarlykov.libsportfolio.ui.adapters.PhotoRvAdapter
import kotlinx.android.synthetic.main.fragment_base.*

typealias ParentDependencies = IDependencies<MainActivityComponent, MainActivityModule>

abstract class TabFragment : Fragment(), ITabFragment {

    abstract val contentType: CONTENT
    abstract var presenter: TabPresenter
    abstract var rvAdapter : PhotoRvAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedtate: Bundle?): View? {
        val layoutId = arguments?.getBundle(KEY_BUNDLE)!!.getInt(KEY_LAYOUT_ID)
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyView()
    }

    override fun updateContent(photos: List<Photo>) {
        (recycleView.adapter as PhotoRvAdapter).updateModel(photos)
    }

    private fun initRecycleView() {

        recycleView.apply {
            setHasFixedSize(true)
            addItemDecoration(GridItemDecoration(2))
            itemAnimator = DefaultItemAnimator()
            layoutManager = GridLayoutManager(activity?.applicationContext, 2)
            adapter = rvAdapter
        }
    }
}