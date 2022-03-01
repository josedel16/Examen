package com.gmail.jmdm1601.examen.ui.images

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gmail.jmdm1601.examen.R
import com.gmail.jmdm1601.examen.databinding.ItemImageBinding

class ImagesAdapter(private var data: List<String>) :
    RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val itemBinding =
            ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = data[position]

        holder.bind(image)
    }

    override fun getItemCount(): Int = data.size

    fun setImages(images: List<String>) {
        this.data = images
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val itemBinding: ItemImageBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(image: String) {
            Glide.with(context)
                .load(image)
                .error(R.drawable.ic_image_not_supported)
                .into(itemBinding.imgPhoto)
        }
    }
}