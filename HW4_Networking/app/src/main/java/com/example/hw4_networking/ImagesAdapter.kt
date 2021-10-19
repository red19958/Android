package com.example.hw4_networking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hw4_networking.databinding.ImageViewBinding

class ImagesAdapter(private val images: List<Image>, private val onClick : (Image) -> Unit) :
    RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val holder = ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.image_view, parent, false))

        holder.root.setOnClickListener{
            onClick(images[holder.adapterPosition])}
        return holder
    }

    class ImageViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        private var binding = ImageViewBinding.bind(root)
        fun bind(image: Image) {
            with(binding) {
                id.text = image.id
                author.text = image.author
                url.text = image.url
            }
        }
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) =
        holder.bind(images[position])

    override fun getItemCount() = images.size
}
