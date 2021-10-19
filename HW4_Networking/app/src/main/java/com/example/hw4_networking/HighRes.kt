package com.example.hw4_networking

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.example.hw4_networking.databinding.HighResBinding
import kotlinx.coroutines.launch

const val URL = "URL"

private var lastBitmap: Bitmap? = null
private var lastUrl: String? = null

class HighRes : AppCompatActivity() {
    lateinit var binding: HighResBinding

    private val boundServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            var bit: Bitmap?
            val extras = intent.extras
            val downloadUrl = extras?.getString("url")
            if (lastUrl != downloadUrl) {
                lastUrl = downloadUrl
                val binderBridge: MyService.MyBinder = service as MyService.MyBinder
                myService = binderBridge.getService()
                isBound = true
                lifecycle.coroutineScope.launch {
                    bit = downloadUrl?.let { myService!!.getImage(it) }
                    lastBitmap = bit
                    binding.imageView.setImageBitmap(bit)
                }
            } else {
                binding.imageView.setImageBitmap(lastBitmap)
            }
            lastUrl = downloadUrl
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HighResBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //lastUrl = savedInstanceState?.getString(URL)
        val intentDownloadImage = Intent(this, MyService::class.java)
        intentDownloadImage.putExtra("url", intentDownloadImage.extras?.getString("url"))
        startService(intentDownloadImage)
        bindService(intentDownloadImage, boundServiceConnection, BIND_AUTO_CREATE)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //outState.putString(URL, lastUrl)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        //lastUrl = savedInstanceState.getString(URL)
    }
}