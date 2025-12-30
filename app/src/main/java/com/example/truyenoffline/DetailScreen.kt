package com.example.truyenoffline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.truyenoffline.model.Story
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, storyId: String?) {
    var story by remember { mutableStateOf<Story?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(storyId) {
        if (storyId != null) {
            val db = Firebase.firestore
            db.collection("stories").document(storyId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        try {
                            val loadedStory = document.toObject(Story::class.java)?.copy(id = document.id)
                            story = loadedStory
                        } catch (e: Exception) { e.printStackTrace() }
                        isLoading = false
                    } else {
                        db.collection("stories").whereEqualTo("slug", storyId).get()
                            .addOnSuccessListener { documents ->
                                if (!documents.isEmpty) {
                                    try {
                                        story = documents.documents[0].toObject(Story::class.java)
                                    } catch (e: Exception) { e.printStackTrace() }
                                }
                                isLoading = false
                            }
                            .addOnFailureListener { isLoading = false }
                    }
                }
                .addOnFailureListener { isLoading = false }
        } else { isLoading = false }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (story != null) story!!.title else "Đang tải...") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (story != null) {
            val currentStory = story!!
            Column(
                modifier = Modifier.padding(innerPadding).fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = currentStory.coverUrl, contentDescription = null,
                    modifier = Modifier.height(260.dp).width(180.dp).clip(RoundedCornerShape(8.dp)).background(Color.Gray),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = currentStory.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(text = "Tác giả: ${currentStory.author}", style = MaterialTheme.typography.titleMedium, color = Color.DarkGray)
                Text(text = "Số chương: ${currentStory.totalChapters}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = { 
                        if (currentStory.slug.isNotEmpty()) {
                            // CHUYỂN "1" DẠNG STRING
                            navController.navigate("read/${currentStory.slug}/1")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("ĐỌC NGAY", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Giới thiệu:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
                Text(text = currentStory.description, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray, modifier = Modifier.padding(top = 8.dp).align(Alignment.Start))
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Không tìm thấy thông tin truyện!") }
        }
    }
}
