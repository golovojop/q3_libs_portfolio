package k.s.yarlykov.libsportfolio.ui.fragments

import android.os.Bundle
import android.view.View
import k.s.yarlykov.libsportfolio.CONTENT
import k.s.yarlykov.libsportfolio.KEY_BUNDLE
import k.s.yarlykov.libsportfolio.di.component.DaggerGalleryFragmentComponent
import k.s.yarlykov.libsportfolio.presenters.TabPresenter
import javax.inject.Inject
import javax.inject.Named

class GalleryTabFragment : TabFragment() {

    override val contentType = CONTENT.GALLERY

    @Inject
    @field:Named("gallery_presenter")
    override lateinit var presenter: TabPresenter

    companion object {

        fun create(bundle: Bundle?): GalleryTabFragment {
            return GalleryTabFragment().apply {
                arguments = Bundle().apply {
                    putBundle(KEY_BUNDLE, bundle)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parent = activity as ParentDependencies

        val component = DaggerGalleryFragmentComponent
            .builder()
            .activityModule(parent.getModule())
            .addDependency(parent.getComponent())
            .bindFragment(this)
            .build()

        component.inject(this)

        presenter.setContentType(contentType)
        presenter.getPhotoObservable().subscribe(photoObserver)
    }
}