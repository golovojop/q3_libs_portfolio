package k.s.yarlykov.libsportfolio.di.component

import dagger.BindsInstance
import dagger.Component
import k.s.yarlykov.libsportfolio.di.component.app.AppComponent
import k.s.yarlykov.libsportfolio.di.module.app.AppModule
import k.s.yarlykov.libsportfolio.di.module.MainActivityModule
import k.s.yarlykov.libsportfolio.di.scope.MainActivityScope
import k.s.yarlykov.libsportfolio.repository.PhotoRepository
import k.s.yarlykov.libsportfolio.ui.MainActivity

@MainActivityScope
@Component(modules = [MainActivityModule::class], dependencies = [AppComponent::class])
interface MainActivityComponent {

    fun inject(activity: MainActivity)

    fun getPhotoRepository() : PhotoRepository

    /**
     * Правила написания кастомного билдера компонента
     * https://dagger.dev/api/latest/dagger/Component.Builder.html
     */
    @Component.Builder
    interface Builder {

        // "Setter" method инжектит инстанс в данный компонент.
        // Теперь его можно использовать, например, передавая в методы модуля.
        @BindsInstance
        fun bindActivity(activity: MainActivity): Builder

        // "Setter" method for each component dependency
        fun addDependency(appComponent: AppComponent): Builder

        // Привязка модуля - фабрики инстансов
        fun activityModule(module: MainActivityModule): Builder
        fun build(): MainActivityComponent
    }
}