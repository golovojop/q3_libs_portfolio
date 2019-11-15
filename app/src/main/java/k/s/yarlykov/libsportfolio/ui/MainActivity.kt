/**
 * Интеграция с Kotlin
 * https://github.com/schoolhelper/MoxyX
 * https://stackoverflow.com/questions/57670510/how-to-get-rid-of-incremental-annotation-processing-requested-warning
 */

package k.s.yarlykov.libsportfolio.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import k.s.yarlykov.libsportfolio.CONTENT
import k.s.yarlykov.libsportfolio.KEY_LAYOUT_ID
import k.s.yarlykov.libsportfolio.R
import k.s.yarlykov.libsportfolio.application.PortfolioApp
import k.s.yarlykov.libsportfolio.di.component.DaggerMainActivityComponent
import k.s.yarlykov.libsportfolio.di.component.MainActivityComponent
import k.s.yarlykov.libsportfolio.di.module.MainActivityModule
import k.s.yarlykov.libsportfolio.presenters.IMainView
import k.s.yarlykov.libsportfolio.presenters.MainPresenter
import k.s.yarlykov.libsportfolio.ui.adapters.CustomFragmentPagerAdapter
import k.s.yarlykov.libsportfolio.ui.fragments.FavoritesTabFragment
import k.s.yarlykov.libsportfolio.ui.fragments.GalleryTabFragment
import k.s.yarlykov.libsportfolio.ui.fragments.InstagramFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity :
    AppCompatActivity(),
    IMainView,
    IDependencies<MainActivityComponent, MainActivityModule> {

    @Inject
    lateinit var presenter: MainPresenter

    private val activityModule : MainActivityModule by lazy (LazyThreadSafetyMode.NONE){
        MainActivityModule()
    }

    private val activityComponent by lazy(LazyThreadSafetyMode.NONE) {
        DaggerMainActivityComponent
            .builder()
            .activityModule(activityModule)
            .addDependency((application as PortfolioApp).appComponent)
            .bindActivity(this)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activityComponent.inject(this)

        fab.setOnClickListener {
            presenter.onFabTapped()
        }

        initTabs()
    }

    override fun getComponent(): MainActivityComponent = activityComponent

    override fun getModule(): MainActivityModule = activityModule

    private fun initTabs() {
        val fragmentPagerAdapter =
            CustomFragmentPagerAdapter(supportFragmentManager)

        fragmentPagerAdapter.addFragment(
            createFragment(CONTENT.GALLERY),
            getString(R.string.tab_text_1)
        )
        fragmentPagerAdapter.addFragment(
            createFragment(CONTENT.FAVORITES),
            getString(R.string.tab_text_2)
        )
        fragmentPagerAdapter.addFragment(
            createFragment(CONTENT.INSTAGRAM, R.layout.fragment_instagram),
            getString(R.string.tab_text_3)
        )

        viewPager.adapter = fragmentPagerAdapter
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)?.icon = getDrawable(R.drawable.ic_photo_library)
        tabs.getTabAt(1)?.icon = getDrawable(R.drawable.ic_photo_favourites)
        tabs.getTabAt(2)?.icon = getDrawable(R.drawable.ic_instagram_logo)

        tabs.tabGravity = TabLayout.GRAVITY_FILL
    }

    private fun createFragment(content: CONTENT, layoutId: Int = R.layout.fragment_base): Fragment {

        return when (content) {
            CONTENT.FAVORITES -> {
                with(Bundle()) {
                    putInt(KEY_LAYOUT_ID, layoutId)
                    FavoritesTabFragment.create(this)
                }
            }
            CONTENT.GALLERY -> {
                with(Bundle()) {
                    putInt(KEY_LAYOUT_ID, layoutId)
                    GalleryTabFragment.create(this)
                }
            }
            CONTENT.INSTAGRAM -> {
                with(Bundle()) {
                    putInt(KEY_LAYOUT_ID, layoutId)
                    InstagramFragment.create(this)
                }
            }
        }
    }

    override fun onClickFabHandler() {
        finish()
    }
}