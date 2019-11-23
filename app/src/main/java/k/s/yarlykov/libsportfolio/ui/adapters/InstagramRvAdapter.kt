package k.s.yarlykov.libsportfolio.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import k.s.yarlykov.libsportfolio.R
import k.s.yarlykov.libsportfolio.logIt

class InstagramRvAdapter(private val itemResourceId: Int) :
    RecyclerView.Adapter<InstagramRvAdapter.ViewHolder>() {

    private val model = mutableListOf<String>()

    init {
        setHasStableIds(true)
    }


    fun updateModel(uri: List<String>) {

        logIt("InstagramRvAdapter::updateModel")
        model.clear()
        model.addAll(uri)
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

    override fun getItemCount(): Int {
        return model.size
    }

    override fun getItemId(position: Int): Long {
        return model[position].hashCode().toLong()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(model[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivPic: ImageView = itemView.findViewById(R.id.iv_instagram_pic)

        fun bind(uri: String) {
            Picasso
                .get()
                .load(uri)
                .into(ivPic)
        }
    }
}