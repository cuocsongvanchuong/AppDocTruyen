package com.example.truyenoffline.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

// --- CẤU HÌNH MỚI THEO LINK BẠN GỬI ---
// Base URL bao gồm cả đoạn "refs/heads/main/"
const val BASE_URL = "https://raw.githubusercontent.com/cuocsongvanchuong/truyen-quoc-dinh-data/refs/heads/main/"

interface GithubApi {
    // Đường dẫn nối tiếp: stories/{slug}/chap-{số}.txt
    // Ví dụ: stories/lo-than-phan.../chap-1.txt
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
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubApi::class.java)
    }
}
