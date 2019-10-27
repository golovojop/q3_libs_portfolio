/**
 * Интеграция с Kotlin
 * https://github.com/schoolhelper/MoxyX
 * https://stackoverflow.com/questions/57670510/how-to-get-rid-of-incremental-annotation-processing-requested-warning
 */

package k.s.yarlykov.libsportfolio.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.material.tabs.TabLayout
import k.s.yarlykov.libsportfolio.CONTENT
import k.s.yarlykov.libsportfolio.KEY_LAYOUT_ID
import k.s.yarlykov.libsportfolio.R
import k.s.yarlykov.libsportfolio.presenters.IMainView
import k.s.yarlykov.libsportfolio.presenters.MainPresenter
import k.s.yarlykov.libsportfolio.ui.fragments.FavouritesTabFragment
import k.s.yarlykov.libsportfolio.ui.fragments.GalleryTabFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : MvpAppCompatActivity(), IMainView {

    @InjectPresenter
    lateinit var presenter : MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            presenter.onFabTapped()
        }

        initTabs()
    }

    private fun initTabs() {
        val fragmentPagerAdapter = CustomFragmentPagerAdapter(supportFragmentManager)

        fragmentPagerAdapter.addFragment(createFragment(CONTENT.GALLERY), getString(R.string.tab_text_1))
        fragmentPagerAdapter.addFragment(createFragment(CONTENT.FAVOURITES), getString(R.string.tab_text_2))

        viewPager.adapter = fragmentPagerAdapter
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)?.icon = getDrawable(R.drawable.ic_photo_library)
        tabs.getTabAt(1)?.icon = getDrawable(R.drawable.ic_photo_favourites)

        tabs.tabGravity = TabLayout.GRAVITY_FILL
    }

    private fun createFragment(content : CONTENT, layoutId : Int = R.layout.fragment_base) : Fragment {

        return when(content) {
            CONTENT.FAVOURITES -> {
                with(Bundle()) {
                    putInt(KEY_LAYOUT_ID, layoutId)
                    FavouritesTabFragment.create(this)
                }
            }
            CONTENT.GALLERY -> {
                with(Bundle()) {
                    putInt(KEY_LAYOUT_ID, layoutId)
                    GalleryTabFragment.create(this)
                }
            }
        }
    }

    override fun onClickFabHandler() {
        finish()
    }

}