package k.s.yarlykov.libsportfolio.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import k.s.yarlykov.libsportfolio.R
import k.s.yarlykov.libsportfolio.model.Photo

class PhotoRvAdapter(private val itemResourceId: Int) : RecyclerView.Adapter<PhotoRvAdapter.ViewHolder>() {

    private val model = mutableListOf<Photo>()

    fun updateModel(photos: List<Photo>) {
        model.clear()
        model.addAll(photos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                itemResourceId,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = model.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(model[position])    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivMain = itemView.findViewById<ImageView>(R.id.iv_rv_item)
        val ivHeart = itemView.findViewById<ImageView>(R.id.iv_heart)
        val ivStar = itemView.findViewById<ImageView>(R.id.iv_star)
        val tvHeart = itemView.findViewById<TextView>(R.id.tv_hearts)

        fun bind(photo: Photo) {
            ivMain.setImageBitmap(photo.bitmap)
            tvHeart.text = photo.likes.toString()
            ivHeart.setImageResource(if(photo.likes > 0) R.drawable.ic_heart_solid else R.drawable.ic_heart)
            ivStar.setImageResource(if(photo.isFavorite) R.drawable.ic_star else R.drawable.ic_star_border)
        }
    }
}