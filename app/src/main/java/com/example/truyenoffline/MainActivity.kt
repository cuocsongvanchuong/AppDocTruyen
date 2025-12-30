package com.example.truyenoffline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.truyenoffline.ui.theme.TruyenOfflineTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TruyenOfflineTheme {
                val navController = rememberNavController()
                
                // Kiem tra xem co dang o man hinh chinh khong de hien BottomBar
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val showBottomBar = currentRoute == "home" || currentRoute == "library" || currentRoute == "profile"

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar(
                                containerColor = Color.White,
                                contentColor = Color(0xFFFBBF24) // Mau Vang Gold
                            ) {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                                    label = { Text("Trang chủ") },
                                    selected = currentRoute == "home",
                                    onClick = { navController.navigate("home") },
                                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFFFBBF24), indicatorColor = Color(0xFFFFF7ED))
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.LibraryBooks, contentDescription = null) },
                                    label = { Text("Tủ sách") },
                                    selected = currentRoute == "library",
                                    onClick = { /* TODO: Lam man hinh Tu Sach */ },
                                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFFFBBF24), indicatorColor = Color(0xFFFFF7ED))
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                                    label = { Text("Cá nhân") },
                                    selected = currentRoute == "profile",
                                    onClick = { /* TODO: Lam man hinh Ca Nhan */ },
                                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFFFBBF24), indicatorColor = Color(0xFFFFF7ED))
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController, 
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") { HomeScreen(navController) }
                        
                        composable("detail/{storyId}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("storyId")
                            DetailScreen(navController, id)
                        }
                        
                        composable(
                            route = "read/{slug}/{chapNum}",
                            arguments = listOf(
                                navArgument("slug") { type = NavType.StringType },
                                navArgument("chapNum") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val slug = backStackEntry.arguments?.getString("slug")
                            val chapNum = backStackEntry.arguments?.getString("chapNum") ?: "1"
                            ChapterScreen(navController, slug, chapNum)
                        }
                    }
                }
            }
        }
    }
}
