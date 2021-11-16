package com.example.hw7_fake_taxi


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hw7_fake_taxi.databinding.PostBinding

class PostsAdapter(
    private val posts: List<Post>,
    private val onClick: (Post) -> Unit
) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
        PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.post, parent, false))


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

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        holder.del.setOnClickListener {
            onClick(posts[holder.adapterPosition])
        }

        return holder.bind(posts[position])
    }


    override fun getItemCount() = this.posts.size
}