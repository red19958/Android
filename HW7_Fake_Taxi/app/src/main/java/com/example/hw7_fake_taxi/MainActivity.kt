package com.example.hw7_fake_taxi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hw7_fake_taxi.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (MyApp.instance.posts.isNotEmpty()) {
            fillRecyclerView()
        } else {
            MyApp.instance.apiService.downloadPosts().enqueue(DownloadCallback())
        }
    }

    inner class CallbackCode : Callback<Post> {

        override fun onFailure(call: Call<Post>?, throwable: Throwable?) =
            Toast.makeText(this@MainActivity, "Problems with connection", Toast.LENGTH_SHORT).show()


        override fun onResponse(call: Call<Post>?, response: Response<Post>?) {
            if (response != null) {
                Toast.makeText(
                    this@MainActivity,
                    "Finished with ${response.code()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    inner class DownloadCallback : Callback<List<Post>> {
        override fun onFailure(call: Call<List<Post>>, throwable: Throwable) =
            Toast.makeText(this@MainActivity, "Problems with connection", Toast.LENGTH_SHORT).show()


        override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
            MyApp.instance.posts = response.body()
            fillRecyclerView()
        }
    }

    fun fillRecyclerView() {
        binding.myRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = PostsAdapter(MyApp.instance.posts) {
                MyApp.instance.apiService.deletePost(it.id.toString()).enqueue(CallbackCode())
            }
        }

        binding.progressBar.visibility = View.INVISIBLE
    }

    fun onClickAddEvent(view: View) {
        if (view is Button) {
            val post = Post(1, "1", "1", 1)
            MyApp.instance.apiService.makePost(post).enqueue(CallbackCode())
        }
    }
}