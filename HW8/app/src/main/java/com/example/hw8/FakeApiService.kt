package com.example.hw8

import retrofit2.Response
import retrofit2.http.*

interface FakeApiService {
    @GET("posts")
    suspend fun downloadPosts(): List<Post>

    @POST("posts")
    suspend fun makePost(@Body post: Post): Response<Post>

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: String): Response<Post>
}