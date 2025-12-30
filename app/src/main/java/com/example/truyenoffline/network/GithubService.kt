package com.example.truyenoffline.network

import com.example.truyenoffline.model.Story
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

// URL Repo cua ban
const val BASE_URL = "https://raw.githubusercontent.com/nguyenquynhduc/truyen-quoc-dinh-data/main/"

interface GithubApi {
    @GET("data/stories.json") 
    suspend fun getStoryList(): List<Story>

    @GET("stories/{slug}/chap-{chapNum}.txt")
    suspend fun getChapterContent(
        @Path("slug") slug: String,
        @Path("chapNum") chapNum: Int
    ): String
}

object RetrofitClient {
    val api: GithubApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubApi::class.java)
    }
}
