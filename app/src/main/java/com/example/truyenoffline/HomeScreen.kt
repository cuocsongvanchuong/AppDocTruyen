package com.example.truyenoffline

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var stories by remember { mutableStateOf<List<Story>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    // Load Data
    LaunchedEffect(Unit) {
        val db = Firebase.firestore
        db.collection("stories")
            .get()
            .addOnSuccessListener { result ->
                try {
                    val list = result.toObjects(Story::class.java)
                    stories = list
                } catch (e: Exception) {
                    errorMessage = "Lỗi data: ${e.message}"
                    stories = sampleStories
                }
                isLoading = false
            }
            .addOnFailureListener { exception ->
                errorMessage = "Offline: ${exception.message}"
                stories = sampleStories
                isLoading = false
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Thư Viện", 
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ) 
                },
                actions = {
                    IconButton(onClick = { /* TODO: Search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(modifier = Modifier.padding(innerPadding)) {
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage, 
                        color = Color.Red, 
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                // GIAO DIEN LUOI (GRID) - Giong Web
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 105.dp), // Tu dong chia cot, toi thieu 105dp/cot
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(stories) { story ->
                        GridStoryItem(story) {
                            val idToPass = if (story.slug.isNotEmpty()) story.slug else story.id
                            navController.navigate("detail/$idToPass")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GridStoryItem(story: Story, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        // 1. ANH BIA (Card bo tron)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f) // Ti le 2:3 chuan truyen
                .shadow(4.dp, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray)
        ) {
            AsyncImage(
                model = story.coverUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Lop phu den mờ o duoi de hien so chuong ro hon
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                        )
                    )
            )

            // Badge so chuong (Goc duoi phai)
            Text(
                text = "${story.totalChapters} chương",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
            )
            
            // Badge FULL (Neu co - Logic gia lap)
            /* if (story.totalChapters > 1000) {
                Text(
                    text = "FULL",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .background(Color(0xFFD32F2F), RoundedCornerShape(bottomEnd = 8.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            */
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 2. TEN TRUYEN
        Text(
            text = story.title,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 13.sp,
                lineHeight = 18.sp
            ),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // 3. TAC GIA
        /*
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = story.author,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 11.sp
        )
        */
    }
}
