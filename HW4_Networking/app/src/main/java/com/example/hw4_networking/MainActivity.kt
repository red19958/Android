package com.example.hw4_networking

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.example.hw4_networking.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.*

const val MESSAGE = "MESSAGE"
var myService: MyService? = null
var isBound = false
class MainActivity : AppCompatActivity() {

    var listOfImages: List<Image> = mutableListOf()
    private var message: String? = null
    private lateinit var binding: ActivityMainBinding
    private val boundServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binderBridge: MyService.MyBinder = service as MyService.MyBinder
            myService = binderBridge.getService()
            isBound = true
            lifecycle.coroutineScope.launch {
                if (message == null) {
                    message = myService!!.getMessage()
                    listOfImages = myService!!.getListOfImages()
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
}