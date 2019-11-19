package k.s.yarlykov.libsportfolio.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import k.s.yarlykov.libsportfolio.CONTENT
import k.s.yarlykov.libsportfolio.KEY_BUNDLE
import k.s.yarlykov.libsportfolio.di.component.DaggerFavoritesFragmentComponent
import k.s.yarlykov.libsportfolio.presenters.TabPresenter
import javax.inject.Inject
import javax.inject.Named

class FavoritesTabFragment : TabFragment() {

    override val contentType = CONTENT.FAVORITES

    @Inject
    @field:Named("favorites_presenter")
    override lateinit var presenter: TabPresenter

    companion object {

        fun create(bundle: Bundle?): FavoritesTabFragment {
            return FavoritesTabFragment().apply {
                arguments = Bundle().apply {
                    putBundle(KEY_BUNDLE, bundle)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val parent = context as ParentDependencies

        val component = DaggerFavoritesFragmentComponent
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
