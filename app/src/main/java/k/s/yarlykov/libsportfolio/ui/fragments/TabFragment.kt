package k.s.yarlykov.libsportfolio.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import k.s.yarlykov.libsportfolio.*
import k.s.yarlykov.libsportfolio.application.IRepositoryHelper
import k.s.yarlykov.libsportfolio.model.Photo
import k.s.yarlykov.libsportfolio.presenters.ITabFragment
import k.s.yarlykov.libsportfolio.presenters.TabPresenter
import k.s.yarlykov.libsportfolio.ui.GridItemDecoration
import k.s.yarlykov.libsportfolio.ui.PhotoRvAdapter
import kotlinx.android.synthetic.main.fragment_base.*

class TabFragment : MvpAppCompatFragment(), ITabFragment {

    companion object {

        fun create(bundle: Bundle?): TabFragment {
            return TabFragment().apply {
                arguments = Bundle().apply {
                    putBundle(KEY_BUNDLE, bundle)
                }
            }
        }
    }

    @InjectPresenter
    lateinit var presenter : TabPresenter

    @ProvidePresenter
    fun providePresenter() : TabPresenter {
        return TabPresenter()
    }

    var category = CATEGORY.REGULAR

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedtate: Bundle?) : View? {

        val layoutId = arguments?.getBundle(KEY_BUNDLE)!!.let { bundle ->
            category = bundle.getParcelable(KEY_CATEGORY) as CATEGORY
            bundle.getInt(KEY_LAYOUT_ID)
        }

        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycleView()

        activity?.let {
            presenter.setPhotoRepository((it.applicationContext as IRepositoryHelper).getPhotoRepository())
        }
    }

    private fun initRecycleView() {

        recycleView.apply {
            setHasFixedSize(true)
            addItemDecoration(GridItemDecoration(2))
            itemAnimator = DefaultItemAnimator()
            layoutManager = GridLayoutManager(activity?.applicationContext, 2)
            adapter = PhotoRvAdapter(R.layout.layout_rv_item)
        }
    }

    override fun updateContent(photos: List<Photo>) {
        (recycleView.adapter as PhotoRvAdapter).updateModel(photos)
    }
}