package com.example.hw7_fake_taxi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hw7_fake_taxi.databinding.PostBinding

class PostsAdapter(
    internal val posts: ArrayList<Post>,
    private val onClick: (Post) -> Unit
) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val holder = PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.post, parent, false)
        )

        holder.del.setOnClickListener {
            onClick(posts[holder.adapterPosition])
            posts.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
            notifyItemRangeChanged(holder.adapterPosition, posts.size)
        }

        return holder
    }


    class PostViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        private var binding = PostBinding.bind(root)
        val del = binding.del
        fun bind(post: Post) {
            with(binding) {
                title.text = post.title
                body.text = post.body
            }
        }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) = holder.bind(posts[position])
    override fun getItemCount() = this.posts.size
}