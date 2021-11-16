package com.example.hw7_fake_taxi

import retrofit2.Call
import retrofit2.http.*

interface FakeTaxiService {
    @GET("/posts")
    fun downloadPosts(): Call<List<Post>>

    @DELETE("/posts/{id}")
    fun deletePost(@Path("id") id : String) : Call<Post>

    @POST("/posts")
    fun makePost(@Body post: Post) : Call<Post>
}