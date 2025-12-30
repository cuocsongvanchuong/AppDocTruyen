package com.example.truyenoffline.model

data class Story(
    val id: String,
    val title: String,       // Tên truyện
    val coverUrl: String,    // Link ảnh bìa
    val author: String,      // Tác giả
    val description: String  // Mô tả ngắn
)

// Tạo danh sách truyện giả để test giao diện
val sampleStories = listOf(
    Story("1", "Dế Mèn Phiêu Lưu Ký", "https://upload.wikimedia.org/wikipedia/vi/6/67/DeMenPhieuLuuKy-Bia1.jpg", "Tô Hoài", "Cuộc phiêu lưu của chú dế mèn..."),
    Story("2", "Đất Rừng Phương Nam", "https://upload.wikimedia.org/wikipedia/vi/a/a2/Dat_rung_phuong_Nam_1997.jpg", "Đoàn Giỏi", "Câu chuyện về cậu bé An..."),
    Story("3", "Truyện Test GitHub", "", "Admin", "Truyện này chưa có ảnh bìa...")
)
