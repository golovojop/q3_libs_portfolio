package k.s.yarlykov.libsportfolio.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import k.s.yarlykov.libsportfolio.CONTENT
import k.s.yarlykov.libsportfolio.KEY_BUNDLE
import k.s.yarlykov.libsportfolio.di.component.ui.DaggerGalleryFragmentComponent
import k.s.yarlykov.libsportfolio.presenters.TabPresenter
import k.s.yarlykov.libsportfolio.ui.adapters.PhotoRvAdapter
import javax.inject.Inject
import javax.inject.Named

class GalleryTabFragment : TabFragment() {

    override val contentType = CONTENT.GALLERY

    @Inject
    @field:Named("gallery_presenter")
    override lateinit var presenter: TabPresenter

    @Inject
    @field:Named("gallery_adapter")
    override lateinit var rvAdapter: PhotoRvAdapter

    companion object {

        fun create(bundle: Bundle?): GalleryTabFragment {
            return GalleryTabFragment().apply {
                arguments = Bundle().apply {
                    putBundle(KEY_BUNDLE, bundle)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val parent = context as ParentDependencies

        val component = DaggerGalleryFragmentComponent
            .builder()
            .activityModule(parent.getModule())
            .addDependency(parent.getComponent())
            .bindFragment(this)
            .build()

        component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreated(contentType)
    }
}