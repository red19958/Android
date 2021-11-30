package com.example.hw8

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import androidx.room.Room
import com.example.hw8.MainActivity.AppDatabase


class MyApp : Application() {

    lateinit var apiService: FakeApiService
        private set
    var post: Post? = null
    private lateinit var database: AppDatabase
    lateinit var postsDao : PostsDao

    override fun onCreate() {
        super.onCreate()
        instance = this
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        apiService = retrofit.create(FakeApiService::class.java)
        database = Room.databaseBuilder(this@MyApp, AppDatabase::class.java, "posts-database").build()
        postsDao = database.postsDao()!!
    }

    companion object {
        lateinit var instance: MyApp
            private set
    }
}