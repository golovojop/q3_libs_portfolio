package k.s.yarlykov.libsportfolio.di.module

import dagger.Module
import dagger.Provides
import k.s.yarlykov.libsportfolio.di.scope.MainActivityScope
import k.s.yarlykov.libsportfolio.presenters.MainPresenter
import k.s.yarlykov.libsportfolio.ui.MainActivity

@Module
class MainActivityModule {

    @MainActivityScope
    @Provides
    fun providePresenter(activity: MainActivity): MainPresenter = MainPresenter(activity)
}