package k.s.yarlykov.libsportfolio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class RVAdapter(private val pics: List<Int>, private val itemResourceId: Int) :
    RecyclerView.Adapter<RVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                itemResourceId,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return pics.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pics[position], position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivMain = itemView.findViewById<ImageView>(R.id.iv_rv_item)
        val ivHeart = itemView.findViewById<ImageView>(R.id.iv_heart)
        val ivStar = itemView.findViewById<ImageView>(R.id.iv_star)
        val tvHeart = itemView.findViewById<TextView>(R.id.tv_hearts)
        val tvStar = itemView.findViewById<TextView>(R.id.tv_stars)

        fun bind(picId: Int, position: Int) {
            ivMain.setImageResource(picId)

            if (position < 3) {
                ivHeart.setImageResource(R.drawable.ic_heart_solid)
                tvHeart.text = getLike()
                ivStar.setImageResource(R.drawable.ic_star)
                tvStar.text = getLike()
            }
        }
    }

    fun getLike() = Random.nextInt(10, 100).toString()
}