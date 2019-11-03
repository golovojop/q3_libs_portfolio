package k.s.yarlykov.libsportfolio.ui

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView

class GridItemDecoration(var columns: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        with(outRect) {
            top = 0
            bottom = 0
            left = 0
            right = 0
        }
        if(columns <= 0 ) return

        val wm = parent.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val parentWidth = with(Point()) {
            wm.defaultDisplay.getSize(this)
            this.x
        }

        view.layoutParams.width = parentWidth/columns
        view.layoutParams.height = view.layoutParams.width
    }
}