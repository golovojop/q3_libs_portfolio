package k.s.yarlykov.libsportfolio.di.module.ui

import dagger.Module
import dagger.Provides
import k.s.yarlykov.libsportfolio.R
import k.s.yarlykov.libsportfolio.di.scope.FragmentScope
import k.s.yarlykov.libsportfolio.di.scope.MainActivityScope
import k.s.yarlykov.libsportfolio.presenters.IPresenter
import k.s.yarlykov.libsportfolio.presenters.InstagramPresenter
import k.s.yarlykov.libsportfolio.presenters.MainPresenter
import k.s.yarlykov.libsportfolio.presenters.TabPresenter
import k.s.yarlykov.libsportfolio.repository.PhotoRepository
import k.s.yarlykov.libsportfolio.ui.MainActivity
import k.s.yarlykov.libsportfolio.ui.adapters.PhotoRvAdapter
import k.s.yarlykov.libsportfolio.ui.fragments.FavoritesTabFragment
import k.s.yarlykov.libsportfolio.ui.fragments.GalleryTabFragment
import javax.inject.Named

@Module
class MainActivityModule {

    private val presenters = HashMap<String, TabPresenter>()

    @MainActivityScope
    @Provides
    fun providePresenter(activity: MainActivity): MainPresenter = MainPresenter(activity)

    @FragmentScope
    @Provides
    @Named("favorites_presenter")
    fun provideFavoritesFragmentPresenter(
        fragment: FavoritesTabFragment,
        photoRepository: PhotoRepository
    ): TabPresenter {

        val key = fragment::class.java.simpleName

        if (!presenters.containsKey(key)) {
            presenters[key] = TabPresenter(fragment, photoRepository)
        }

        return presenters[key] as TabPresenter
    }

    @FragmentScope
    @Provides
    @Named("gallery_presenter")
    fun provideGalleryFragmentPresenter(
        fragment: GalleryTabFragment,
        photoRepository: PhotoRepository
    ): TabPresenter {
        val key = fragment::class.java.simpleName

        if (!presenters.containsKey(key)) {
            presenters[key] = TabPresenter(fragment, photoRepository)
        }

        return presenters[key] as TabPresenter
    }
}