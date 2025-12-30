package com.example.truyenoffline.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory // Import moi
import retrofit2.http.GET
import retrofit2.http.Path

// URL Repo cua ban
const val BASE_URL = "https://raw.githubusercontent.com/cuocsongvanchuong/truyen-quoc-dinh-data/refs/heads/main/"

interface GithubApi {
    @GET("stories/{slug}/chap-{chapNum}.txt")
    suspend fun getChapterContent(
        @Path("slug") slug: String,
        @Path("chapNum") chapNum: String
    ): String
}

object RetrofitClient {
    val api: GithubApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // QUAN TRONG: Them Scalars truoc Gson
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubApi::class.java)
    }
}
