package com.example.hw7_fake_taxi

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MyApp : Application() {
    lateinit var apiService : FakeTaxiService
    var posts : List<Post>? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        apiService = retrofit.create(FakeTaxiService::class.java)
    }

    companion object {
        lateinit var instance: MyApp
            private set
    }
}