package com.example.truyenoffline.model

import com.google.gson.annotations.SerializedName

data class Story(
    val id: String,
    val title: String,
    val author: String,
    val coverUrl: String,
    val description: String,
    
    @SerializedName("slug")
    val slug: String = "",
    
    @SerializedName("chap_count")
    val totalChapters: Int = 0
)

val sampleStories = listOf(
    Story("1", "Dế Mèn Phiêu Lưu Ký", "Tô Hoài", "https://upload.wikimedia.org/wikipedia/vi/6/67/DeMenPhieuLuuKy-Bia1.jpg", "Truyện thiếu nhi...", "de-men", 10),
    Story("2", "Đất Rừng Phương Nam", "Đoàn Giỏi", "https://upload.wikimedia.org/wikipedia/vi/a/a2/Dat_rung_phuong_Nam_1997.jpg", "Bối cảnh Nam Bộ...", "dat-rung", 20)
)
