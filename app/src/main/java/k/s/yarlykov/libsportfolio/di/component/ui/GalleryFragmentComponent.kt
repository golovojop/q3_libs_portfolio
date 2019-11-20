package k.s.yarlykov.libsportfolio.di.component.ui

import dagger.BindsInstance
import dagger.Component
import k.s.yarlykov.libsportfolio.di.module.MainActivityModule
import k.s.yarlykov.libsportfolio.di.scope.FragmentScope
import k.s.yarlykov.libsportfolio.ui.fragments.GalleryTabFragment

@FragmentScope
@Component(modules = [MainActivityModule::class], dependencies = [MainActivityComponent::class])
interface GalleryFragmentComponent {

    fun inject(fragment: GalleryTabFragment)

    @Component.Builder
    interface Builder {

        // "Setter" method инжектит инстанс в данный компонент.
        // Теперь его можно использовать, например, передавая в методы модуля.
        @BindsInstance
        fun bindFragment(fragment: GalleryTabFragment): Builder

        // "Setter" method for each component dependency
        fun addDependency(activityComponent: MainActivityComponent): Builder

        // Привязка модуля - фабрики инстансов
        fun activityModule(module: MainActivityModule): Builder
        fun build(): GalleryFragmentComponent
    }
}