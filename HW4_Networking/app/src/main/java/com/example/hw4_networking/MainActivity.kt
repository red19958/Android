package com.example.hw4_networking

import android.content.*
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.example.hw4_networking.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    lateinit var myService: MyService
    var isBound = false
    var listOfImages: List<Image> = mutableListOf()
    private var message: String? = null
    private lateinit var binding: ActivityMainBinding
    private val boundServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val scope = CoroutineScope(Dispatchers.Main)
            scope.launch {
                val binderBridge: MyService.MyBinder = service as MyService.MyBinder
                myService = binderBridge.getService()
                isBound = true
                if (message == null) {
                    message = myService.getMessage()
                    listOfImages = myService.getListOfImages()
                } else {
                    listOfImages = fillRecyclerView(message!!)
                }
                binding.myRecyclerView.apply{
                    adapter = ImagesAdapter(listOfImages){
                        val intent = Intent(this@MainActivity, HighRes::class.java)
                        intent.putExtra("url", it.download_url)
                        startActivity(intent)
                    }
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
        }
    }

    private fun fillRecyclerView(string: String): List<Image> {
        return Gson().fromJson(string, Array<Image>::class.java).toList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        message = savedInstanceState?.getString(MESSAGE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = Intent(this, MyService::class.java)
        intent.putExtra("url", "https://picsum.photos/v2/list?limit=100")
        startService(intent)
        bindService(intent, boundServiceConnection, BIND_AUTO_CREATE)
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, MyService::class.java)
        startService(intent)
        bindService(intent, boundServiceConnection, BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(boundServiceConnection)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(MESSAGE, message)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        message = savedInstanceState.getString(MESSAGE)
    }

    companion object {
        const val MESSAGE = "MESSAGE"
    }
}