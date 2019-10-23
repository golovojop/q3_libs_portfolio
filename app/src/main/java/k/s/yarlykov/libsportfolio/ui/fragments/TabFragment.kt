package k.s.yarlykov.libsportfolio.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import k.s.yarlykov.libsportfolio.*
import k.s.yarlykov.libsportfolio.ui.GridItemDecoration
import k.s.yarlykov.libsportfolio.ui.RVAdapter
import kotlinx.android.synthetic.main.fragment_base.*
import java.util.*

class TabFragment : Fragment() {

    companion object {

        fun create(bundle: Bundle?): TabFragment {
            return TabFragment().apply {
                arguments = Bundle().apply {
                    putBundle(KEY_BUNDLE, bundle)
                }
            }
        }
    }

    var season = CATEGORY.REGULAR

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedtate: Bundle?) : View? {

        val layoutId = arguments?.getBundle(KEY_BUNDLE)!!.let { bundle ->
            season = bundle.getSerializable(KEY_SEASON) as CATEGORY
            bundle.getInt(KEY_LAYOUT_ID)
        }

        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
    }

    private fun loadData() {
        val stuffPics = with(resources.obtainTypedArray(R.array.month_pics)) {

            mutableListOf<Int>().also { li ->
                (0 until length()).forEach { i ->
                    li.add(i, getResourceId(i, R.drawable.bkg_01_jan))
                }
                recycle()
            }
        }
        Collections.rotate(stuffPics, 12 - season.ordinal * 3)
        initRecycleView(stuffPics)
    }

    private fun initRecycleView(data : List<Int>) {

        recycle_view.apply {
            setHasFixedSize(true)
            addItemDecoration(GridItemDecoration(2))
            itemAnimator = DefaultItemAnimator()
            layoutManager = GridLayoutManager(activity?.applicationContext, 2)
            adapter = RVAdapter(data, R.layout.layout_rv_item)
        }

    }
}