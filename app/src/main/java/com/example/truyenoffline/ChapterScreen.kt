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
fun ChapterScreen(navController: NavController, slug: String?, chapNumStr: String) {
    var content by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    // Tự động tải khi mở màn hình
    LaunchedEffect(slug, chapNumStr) {
        if (slug != null) {
            isLoading = true
            scope.launch {
                try {
                    // Gọi API lấy text
                    val text = RetrofitClient.api.getChapterContent(slug, chapNumStr)
                    content = text
                } catch (e: Exception) {
                    content = "Không tải được nội dung.\n\n1. Kiểm tra mạng.\n2. Kiểm tra file trên GitHub: stories/$slug/chap-$chapNumStr.txt có tồn tại không?\n3. Lỗi: ${e.message}"
                } finally {
                    isLoading = false
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chương $chapNumStr") },
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
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                Text(
                    text = content,
                    fontSize = 18.sp,
                    lineHeight = 28.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                // Logic tính toán chương tiếp theo (mô phỏng parseFloat của JS)
                val currentNum = chapNumStr.toDoubleOrNull() ?: 1.0
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 48.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        enabled = currentNum > 1,
                        onClick = { 
                            // Nếu là số nguyên thì bỏ .0 (VD: 1.0 -> 1)
                            val prev = removeZero(currentNum - 1)
                            navController.navigate("read/$slug/$prev") 
                        }
                    ) {
                        Text("Chương trước")
                    }
                    
                    Button(
                        onClick = { 
                            val next = removeZero(currentNum + 1)
                            navController.navigate("read/$slug/$next") 
                        }
                    ) {
                        Text("Chương sau")
                    }
                }
            }
        }
    }
}

// Hàm phụ trợ: 1.0 -> "1", 1.5 -> "1.5"
fun removeZero(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        value.toString()
    }
}
