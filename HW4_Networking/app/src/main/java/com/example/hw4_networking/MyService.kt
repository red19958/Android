package com.example.hw4_networking

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import com.google.gson.Gson
import java.io.InputStreamReader
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.*



class MyService : Service() {
    private var lastBitmap: Bitmap? = null
    private var lastUrl: String? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        return MyBinder()
    }

    inner class MyBinder : Binder() {
        fun getService() = this@MyService
    }

    suspend fun getMessage(): String? {
        var message: String? = null
        return try {
            withContext(Dispatchers.IO) {
                val httpUrlConnection = URL(url).openConnection() as HttpURLConnection
                val streamReader = InputStreamReader(httpUrlConnection.inputStream)
                val text = streamReader.readText()
                httpUrlConnection.disconnect()
                message = text
            }
            message
        } catch (e: IOException) {
            e.printStackTrace()
            message
        }
    }

    suspend fun getListOfImages(): List<Image> {
        var images: List<Image> = mutableListOf()
        return try {
            withContext(Dispatchers.IO) {
                val httpUrlConnection = URL(url).openConnection() as HttpURLConnection
                val streamReader = InputStreamReader(httpUrlConnection.inputStream)
                val text = streamReader.readText()
                val imageList: List<Image> =
                    Gson().fromJson(text, Array<Image>::class.java).toList()
                httpUrlConnection.disconnect()
                images = imageList
            }
            images
        } catch (e: IOException) {
            e.printStackTrace()
            images
        }
    }

    suspend fun getImage(link: String): Bitmap? {
        var bitmapImage: Bitmap? = null
        return try {
            if (lastUrl != link) {
                bitmapImage = withContext(Dispatchers.IO) {
                    val inputStream = URL(link).openStream()
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream.close()
                    lastBitmap = bitmap
                    lastUrl = link
                    bitmap
                }
            } else {
                bitmapImage = lastBitmap
            }
            bitmapImage
        } catch (e: IOException) {
            e.printStackTrace()
            bitmapImage
        }
    }

    companion object {
        const val url = "https://picsum.photos/v2/list?limit=100"
    }
}
