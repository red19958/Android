package com.example.hw8

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.coroutineScope
import com.example.hw8.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.room.Database
import androidx.room.RoomDatabase
import kotlinx.coroutines.async


class MainActivity : AppCompatActivity() {

    @Database(entities = [Post::class], version = 1)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun postsDao(): PostsDao?
    }

    private fun clearDatabase() {
        lifecycle.coroutineScope.launch {
            val posts = MyApp.instance.postsDao.getAll()
            for (post in posts) {
                MyApp.instance.postsDao.deletePost(post)
            }
        }
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PostsAdapter
    private var dat: ArrayList<Post>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val arguments = intent.extras

        if (arguments != null  && MyApp.instance.post != null) {
            MyApp.instance.post.let { addPost(it!!) }
        }

        binding.progressBar.isVisible = true
        binding.add.isVisible = false

        lifecycle.coroutineScope.launch {
            dat = MyApp.instance.postsDao.getAll() as ArrayList<Post>?
            val data = async {
                try {
                    MyApp.instance.apiService.downloadPosts()
                } catch (e: Exception) {

                    Toast.makeText(
                        this@MainActivity,
                        "Problems with connection",
                        Toast.LENGTH_SHORT
                    ).show()
                    arrayListOf(Post(1, "title", "body", 1))
                }
            }

            if (dat == null) {
                dat = data.await() as ArrayList<Post>?
                for (post in dat!!) {
                    MyApp.instance.postsDao.insertPost(post)
                }
            }

            adapter = PostsAdapter(dat!!) {
                lifecycle.coroutineScope.launch {
                    try {
                        MyApp.instance.apiService.deletePost(it.id.toString())
                        MyApp.instance.postsDao.deletePost(it)
                        Toast.makeText(
                            this@MainActivity,
                            "Removed",
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        MyApp.instance.postsDao.deletePost(it)
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

    private fun addPost(post: Post) {
        lifecycle.coroutineScope.launch {
            try {
                MyApp.instance.apiService.makePost(post)
                MyApp.instance.postsDao.insertPost(post)
                adapter.posts.add(1, post)
                adapter.notifyItemInserted(1)
                intent.removeExtra("added")
            } catch (e: Exception) {
                MyApp.instance.postsDao.insertPost(post)
                adapter.posts.add(1, post)
                adapter.notifyItemInserted(1)
                intent.removeExtra("added")
                Toast.makeText(
                    this@MainActivity,
                    "Problems with connection",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    fun onAddClickEvent(view: View) {
        if (view is Button) {
            val intent = Intent(this@MainActivity, AddActivity::class.java)
            startActivity(intent)
        }
    }

    fun onReloadClickEvent(view: View) {
        if (view is Button) {
            clearDatabase()
            lifecycle.coroutineScope.launch {
                binding.myRecyclerView.isVisible = false
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
                        arrayListOf(Post(1, "title", "body", 1))
                    }
                }
                dat = data.await() as ArrayList<Post>?

                for (post in dat!!) {
                    MyApp.instance.postsDao.insertPost(post)
                }

                adapter = PostsAdapter(dat!!) {
                    lifecycle.coroutineScope.launch {
                        try {
                            MyApp.instance.apiService.deletePost(it.id.toString())
                            MyApp.instance.postsDao.deletePost(it)
                            Toast.makeText(
                                this@MainActivity,
                                "Removed",
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
                binding.myRecyclerView.isVisible = true
                binding.progressBar.isVisible = false
                binding.add.isVisible = true
            }
        }

        Toast.makeText(
            this@MainActivity,
            "Already reset",
            Toast.LENGTH_SHORT
        ).show()
    }
}