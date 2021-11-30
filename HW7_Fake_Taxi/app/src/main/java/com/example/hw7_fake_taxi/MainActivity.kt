package com.example.hw7_fake_taxi

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.coroutineScope
import com.example.hw7_fake_taxi.databinding.ActivityMainBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycle.coroutineScope.launch {
            binding.progressBar.isVisible = true
            binding.add.isVisible = false

            val data = async {
                try {
                    MyApp.instance.apiService.downloadPosts()
                } catch (e: Exception) {

                    Toast.makeText(
                        this@MainActivity,
                        "Problems with connection",
                        Toast.LENGTH_SHORT
                    ).show()

                    if (MyApp.instance.posts == null) error(applicationContext)
                    arrayListOf(Post(1, "title", "body", 1))
                }
            }

            if (MyApp.instance.posts == null)
                MyApp.instance.posts = data.await()

            adapter = PostsAdapter(MyApp.instance.posts!! as ArrayList<Post>) {
                lifecycle.coroutineScope.launch {
                    try {
                        val response = MyApp.instance.apiService.deletePost(it.id.toString())
                        Toast.makeText(
                            this@MainActivity,
                            "Finished with ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@MainActivity,
                            "Problems with connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            binding.myRecyclerView.adapter = adapter
            binding.progressBar.isVisible = false
            binding.add.isVisible = true
        }
    }

    fun onClickAddEvent(view: View) {
        if (view is Button) {
            val post = Post(1, "1", "1", 1)

            lifecycle.coroutineScope.launch {
                try {
                    val response = MyApp.instance.apiService.makePost(post)
                    Toast.makeText(
                        this@MainActivity,
                        "Finished with ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(
                        this@MainActivity,
                        "Problems with connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            adapter.posts.add(0, post)
            adapter.notifyItemInserted(0)
        }
    }
}