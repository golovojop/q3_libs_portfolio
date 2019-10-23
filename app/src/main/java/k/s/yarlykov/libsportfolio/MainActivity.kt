package k.s.yarlykov.libsportfolio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import k.s.yarlykov.libsportfolio.fragments.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            finish()
        }

        initTabs()
    }

    private fun initTabs() {
        val sectionsPagerAdapter = CustomFragmentPagerAdapter(supportFragmentManager)

        sectionsPagerAdapter.addFragment(createFragment(CATEGORY.REGULAR), getString(R.string.tab_text_1))
        sectionsPagerAdapter.addFragment(createFragment(CATEGORY.FAVOURITES), getString(R.string.tab_text_2))

        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)

        tabs.getTabAt(0)?.icon = getDrawable(R.drawable.ic_photo_library)
        tabs.getTabAt(1)?.icon = getDrawable(R.drawable.ic_photo_favourites)

        tabs.tabGravity = TabLayout.GRAVITY_FILL
    }

    private fun createFragment(season : CATEGORY, layoutId : Int = R.layout.fragment_base) : Fragment {
        return with(Bundle()) {
            putInt(KEY_LAYOUT_ID, layoutId)
            putSerializable(KEY_SEASON, season)
            BaseFragment.create(this)
        }
    }
}