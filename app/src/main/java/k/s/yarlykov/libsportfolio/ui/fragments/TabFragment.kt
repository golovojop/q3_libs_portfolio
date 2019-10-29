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
import com.arellomobile.mvp.presenter.ProvidePresenterTag
import io.reactivex.disposables.CompositeDisposable
import k.s.yarlykov.libsportfolio.*
import k.s.yarlykov.libsportfolio.application.IRepositoryHelper
import k.s.yarlykov.libsportfolio.model.Photo
import k.s.yarlykov.libsportfolio.presenters.ITabFragment
import k.s.yarlykov.libsportfolio.presenters.TabPresenter
import k.s.yarlykov.libsportfolio.ui.GridItemDecoration
import k.s.yarlykov.libsportfolio.ui.PhotoRvAdapter
import kotlinx.android.synthetic.main.fragment_base.*

abstract class TabFragment : MvpAppCompatFragment(), ITabFragment {

    abstract val contentType: CONTENT

    val disposables = CompositeDisposable()

    @InjectPresenter
    open lateinit var presenter: TabPresenter

    @ProvidePresenter
    open fun providePresenter(): TabPresenter {
        return TabPresenter().apply {
            setContentType(contentType)
        }
    }

    @ProvidePresenterTag(presenterClass = TabPresenter::class)
    open fun providePresenterTag(): String = contentType.name

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedtate: Bundle?): View? {

        val layoutId = arguments?.getBundle(KEY_BUNDLE)!!.let { bundle ->
            bundle.getInt(KEY_LAYOUT_ID)
        }

        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycleView()

        activity?.let {

            disposables.add(
                presenter
                    .setPhotoRepository((it.applicationContext as IRepositoryHelper).getPhotoRepository())
                    .subscribe(::updateContent)
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        disposables.dispose()
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