package com.example.truyenoffline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.truyenoffline.ui.theme.TruyenOfflineTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TruyenOfflineTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "home") {
                        // 1. Màn hình chính
                        composable("home") {
                            HomeScreen(navController)
                        }
                        
                        // 2. Màn hình chi tiết
                        composable("detail/{storyId}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("storyId")
                            DetailScreen(navController, id)
                        }
                        
                        // 3. Màn hình đọc truyện (Mới)
                        composable(
                            route = "read/{slug}/{chapNum}",
                            arguments = listOf(
                                navArgument("slug") { type = NavType.StringType },
                                navArgument("chapNum") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val slug = backStackEntry.arguments?.getString("slug")
                            val chapNum = backStackEntry.arguments?.getInt("chapNum") ?: 1
                            ChapterScreen(navController, slug, chapNum)
                        }
                    }
                }
            }
        }
    }
}
