package k.s.yarlykov.libsportfolio.di.component

import android.content.Context
import com.google.gson.Gson
import dagger.BindsInstance
import dagger.Component
import k.s.yarlykov.libsportfolio.di.component.app.AppComponent
import k.s.yarlykov.libsportfolio.di.module.MainActivityModule
import k.s.yarlykov.libsportfolio.di.scope.MainActivityScope
import k.s.yarlykov.libsportfolio.repository.PhotoRepository
import k.s.yarlykov.libsportfolio.ui.MainActivity
import okhttp3.OkHttpClient
import retrofit2.CallAdapter

@MainActivityScope
@Component(modules = [MainActivityModule::class], dependencies = [AppComponent::class])
interface MainActivityComponent {

    fun inject(activity: MainActivity)

    fun getContext() : Context

    fun getOkhttp3Cache() : okhttp3.Cache
    fun getGson() : Gson
    fun getOkHttpClient() : OkHttpClient
    fun getCallAdapterFactory() : CallAdapter.Factory

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