package com.example.truyenoffline.model

import com.google.gson.annotations.SerializedName

data class Story(
    val id: String = "",
    val title: String = "",
    val author: String = "",
    val coverUrl: String = "",
    val description: String = "",
    
    @SerializedName("slug")
    val slug: String = "",
    
    @SerializedName("chap_count")
    val totalChapters: Int = 0
)

// List mau (Fallback)
val sampleStories = listOf(
    Story("1", "Dế Mèn (Mẫu)", "Tô Hoài", "https://upload.wikimedia.org/wikipedia/vi/6/67/DeMenPhieuLuuKy-Bia1.jpg", "Truyện mẫu...", "de-men", 10)
)
