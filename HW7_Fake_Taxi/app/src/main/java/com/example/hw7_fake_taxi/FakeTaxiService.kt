package com.example.hw7_fake_taxi

import retrofit2.Response
import retrofit2.http.*

interface FakeTaxiService {
    @GET("/posts")
    suspend fun downloadPosts(): List<Post>

    @DELETE("/posts/{id}")
    suspend fun deletePost(@Path("id") id : String) : Response<Post>

    @POST("/posts")
    suspend fun makePost(@Body post: Post) : Response<Post>
}