package com.example.hw4_networking

import android.content.*
import android.graphics.Bitmap
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.example.hw4_networking.databinding.HighResBinding
import kotlinx.coroutines.*


class HighRes : AppCompatActivity() {
    lateinit var myService: MyService
    var isBound = false
    lateinit var binding: HighResBinding
    private val boundServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            var bit: Bitmap?
            val extras = intent.extras
            val downloadUrl = extras?.getString("url")
            val binderBridge: MyService.MyBinder = service as MyService.MyBinder
            myService = binderBridge.getService()
            isBound = true
            val scope = CoroutineScope(Dispatchers.Main)
            scope.launch {
                bit = downloadUrl?.let { myService.getImage(it) }
                binding.imageView.setImageBitmap(bit)
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HighResBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intentDownloadImage = Intent(this, MyService::class.java)
        intentDownloadImage.putExtra("url", intentDownloadImage.extras?.getString("url"))
        startService(intentDownloadImage)
        bindService(intentDownloadImage, boundServiceConnection, BIND_AUTO_CREATE)
    }
}