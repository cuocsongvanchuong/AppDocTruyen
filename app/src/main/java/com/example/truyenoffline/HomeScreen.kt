package com.example.truyenoffline

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.truyenoffline.model.Story
import com.example.truyenoffline.model.sampleStories
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

// MAU SAC CHUAN CUA WEB PRO35
val ProGold = Color(0xFFFBBF24)
val ProBg = Color(0xFFF9FAFB)
val ProText = Color(0xFF1F2937)

@Composable
fun HomeScreen(navController: NavController) {
    var stories by remember { mutableStateOf<List<Story>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val db = Firebase.firestore
        db.collection("stories").limit(20).get()
            .addOnSuccessListener { result ->
                try {
                    stories = result.toObjects(Story::class.java)
                } catch (e: Exception) { stories = sampleStories }
                isLoading = false
            }
            .addOnFailureListener {
                stories = sampleStories
                isLoading = false
            }
    }

    // GIAO DIEN CHINH
    Column(modifier = Modifier.fillMaxSize().background(ProBg)) {
        // 1. Header (Logo + Search)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "TRUYENDICHPRO", color = ProGold, fontWeight = FontWeight.Black, fontSize = 20.sp)
            Row {
                Icon(Icons.Default.Search, contentDescription = null, tint = ProText, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Default.Notifications, contentDescription = null, tint = ProText, modifier = Modifier.size(24.dp))
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = ProGold)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                
                // 2. BANNER (Truyen noi bat nhat) - Lay truyen dau tien
                if (stories.isNotEmpty()) {
                    item {
                        BannerSection(stories.first()) {
                            val id = if (stories.first().slug.isNotEmpty()) stories.first().slug else stories.first().id
                            navController.navigate("detail/$id")
                        }
                    }
                }

                // 3. TRUYEN DE CU (Luot Ngang)
                item {
                    SectionHeader("Truyện Đề Cử")
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(stories.take(5)) { story -> // Lay 5 truyen dau
                            CardItemHorizontal(story) {
                                val id = if (story.slug.isNotEmpty()) story.slug else story.id
                                navController.navigate("detail/$id")
                            }
                        }
                    }
                }

                // 4. MOI CAP NHAT (Danh sach doc - Style List View)
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    SectionHeader("Mới Cập Nhật")
                }
                
                items(stories) { story ->
                    CardItemVertical(story) {
                        val id = if (story.slug.isNotEmpty()) story.slug else story.id
                        navController.navigate("detail/$id")
                    }
                }
                
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}

// --- CAC COMPONENT CON ---

@Composable
fun SectionHeader(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = ProText)
        Text(text = "Xem thêm >", color = ProGold, fontSize = 12.sp)
    }
}

// Banner to o tren cung
@Composable
fun BannerSection(story: Story, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = story.coverUrl, contentDescription = null,
            modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop
        )
        // Lop phu den
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)))
        
        Column(
            modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
        ) {
            Text(
                text = "TRUYỆN NỔI BẬT", 
                color = ProGold, 
                fontSize = 10.sp, 
                fontWeight = FontWeight.Bold,
                modifier = Modifier.background(Color.Black, RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = story.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

// Card dung cho list luot ngang (De cu)
@Composable
fun CardItemHorizontal(story: Story, onClick: () -> Unit) {
    Column(
        modifier = Modifier.width(110.dp).clickable { onClick() }
    ) {
        AsyncImage(
            model = story.coverUrl, contentDescription = null,
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = story.title, maxLines = 2, overflow = TextOverflow.Ellipsis, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, lineHeight = 18.sp)
    }
}

// Card ngang cho list doc (Moi cap nhat)
@Composable
fun CardItemVertical(story: Story, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Anh nho ben trai
        AsyncImage(
            model = story.coverUrl, contentDescription = null,
            modifier = Modifier
                .width(70.dp)
                .height(100.dp)
                .clip(RoundedCornerShape(6.dp)),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Thong tin ben phai
        Column(modifier = Modifier.weight(1f)) {
            Text(text = story.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = story.author.ifEmpty { "Đang cập nhật" }, fontSize = 12.sp, color = Color.Gray)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Badge chuong
            Row {
                Text(
                    text = "Chương ${story.totalChapters}",
                    fontSize = 11.sp,
                    color = ProGold,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .background(ProGold.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
    }
}
