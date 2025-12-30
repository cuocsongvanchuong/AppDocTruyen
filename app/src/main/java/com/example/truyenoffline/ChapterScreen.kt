package com.example.truyenoffline

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.truyenoffline.network.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterScreen(navController: NavController, slug: String?, chapterNum: Int) {
    var content by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    // Tự động tải nội dung chương khi mở
    LaunchedEffect(slug, chapterNum) {
        if (slug != null) {
            scope.launch {
                try {
                    // Gọi GitHub lấy file txt: stories/{slug}/chap-{num}.txt
                    val text = RetrofitClient.api.getChapterContent(slug, chapterNum)
                    content = text
                } catch (e: Exception) {
                    content = "Không tải được chương này. Có thể file chưa được up lên GitHub?\nLỗi: ${e.message}"
                } finally {
                    isLoading = false
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chương $chapterNum") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(scrollState) // Cho phép cuộn dọc
                    .padding(16.dp)
            ) {
                Text(
                    text = content,
                    fontSize = 18.sp,
                    lineHeight = 28.sp, // Khoảng cách dòng cho dễ đọc
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                // Nút chuyển chương (Đơn giản)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        enabled = chapterNum > 1,
                        onClick = { navController.navigate("read/$slug/${chapterNum - 1}") }
                    ) {
                        Text("Chương trước")
                    }
                    
                    Button(
                        onClick = { navController.navigate("read/$slug/${chapterNum + 1}") }
                    ) {
                        Text("Chương sau")
                    }
                }
            }
        }
    }
}
