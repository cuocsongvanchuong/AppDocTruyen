package com.example.truyenoffline.network

import com.example.truyenoffline.model.Story
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

// --- CẤU HÌNH QUAN TRỌNG ---
// Bạn hãy thay thông tin khớp với APP_CONFIG trong file JS
// Ví dụ: https://raw.githubusercontent.com/TAI_KHOAN_CUA_BAN/TEN_REPO/TEN_NHANH/
const val BASE_URL = "https://raw.githubusercontent.com/nguyenquynhduc/truyen-quoc-dinh-data/main/" 

interface GithubApi {
    // Lấy nội dung chương
    // Logic JS: stories/${slug}/chap-${numInput}.txt
    // Ở đây ta dùng String cho chapNum để hỗ trợ cả chương 1.5, 10-1...
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
