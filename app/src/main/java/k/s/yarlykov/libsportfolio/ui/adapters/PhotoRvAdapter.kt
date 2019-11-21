package k.s.yarlykov.libsportfolio.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import k.s.yarlykov.libsportfolio.R
import k.s.yarlykov.libsportfolio.domain.room.Photo
import k.s.yarlykov.libsportfolio.presenters.ITabPresenter

class PhotoRvAdapter(private val itemResourceId: Int, private val presenter: ITabPresenter) :
    RecyclerView.Adapter<PhotoRvAdapter.ViewHolder>() {

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
        holder.bind(model[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivMain = itemView.findViewById<ImageView>(R.id.iv_rv_item)
        private val ivLike = itemView.findViewById<ImageView>(R.id.iv_like)
        private val ivFavorite = itemView.findViewById<ImageView>(R.id.iv_favorite)
        private val tvLikes = itemView.findViewById<TextView>(R.id.tv_likes)

        fun bind(photo: Photo) {
            ivMain.setImageBitmap(photo.bitmap)
            tvLikes.text = photo.likes.toString()

            with(ivLike) {
                setImageResource(if (photo.likes > 0) R.drawable.ic_like_solid else R.drawable.ic_like)
                setOnClickListener {
                    presenter.onClickLikeButton(adapterPosition)
                }
            }

            with(ivFavorite) {
                setImageResource(if (photo.favorite) R.drawable.ic_favorite_solid else R.drawable.ic_favorite)
                setOnClickListener {
                    presenter.onClickFavoriteButton(adapterPosition)
                }
            }
        }
    }
}