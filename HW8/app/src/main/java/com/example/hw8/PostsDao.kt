package com.example.hw8

import androidx.room.*

@Dao
interface PostsDao {
    @Query("select * from post")
    suspend fun getAll() : List<Post>

    @Delete
    suspend fun deletePost(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post)
}