package k.s.yarlykov.libsportfolio.di.component.ui

import dagger.BindsInstance
import dagger.Component
import k.s.yarlykov.libsportfolio.di.module.instagram.InstagramModule
import k.s.yarlykov.libsportfolio.di.scope.InstagramScope
import k.s.yarlykov.libsportfolio.ui.fragments.InstagramFragment

@InstagramScope
@Component(modules = [InstagramModule::class], dependencies = [MainActivityComponent::class])
interface InstagramComponent {

    fun inject(fragment : InstagramFragment)

    @Component.Builder
    interface Builder {

        // "Setter" method инжектит инстанс в данный компонент.
        // Теперь его можно использовать, например, передавая в методы модуля.
        @BindsInstance
        fun bindFragment(fragment: InstagramFragment): Builder

        // "Setter" method for each component dependency
        fun addDependency(activityComponent: MainActivityComponent): Builder

        // Привязка модуля - фабрики инстансов
        fun instagramModule(module: InstagramModule): Builder
        fun build(): InstagramComponent
    }
}