package com.example.dacs3

// ✅ Nhóm 1: Android Core (Các thành phần cơ bản của Android)
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

// ✅ Nhóm 2: Jetpack Compose Navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavController

// ✅ Nhóm 3: Compose UI (Các thành phần giao diện trong Jetpack Compose)
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.dacs3.ui.HomeScreen
import com.example.dacs3.ui.CardMovieScreen // 🔹 Thêm màn hình CardMovieScreen

// ✅ Nhóm 4: Material Icons (Các biểu tượng trong Material Design)
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.navArgument

// ✅ Lớp MainActivity là điểm khởi đầu của ứng dụng
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Hỗ trợ UI toàn màn hình (Edge-to-Edge)
        setContent {
            MyApp() // Gọi hàm chính của giao diện
        }
    }
}

// ✅ MyApp(): Hàm chính để hiển thị giao diện ứng dụng
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    val navController = rememberNavController() // 🔹 Thêm NavController để điều hướng

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.logo), // Thay "logo" bằng tên tệp ảnh trong drawable
                        contentDescription = "Logo Rạp Phim",
                        modifier = Modifier
                            .height(50.dp)
                            .clickable{
                                navController.navigate("home")
                            }

                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF952531),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    var showMenu by remember { mutableStateOf(false) }
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(text = { Text("Lịch chiếu") }, onClick = { showMenu = false })
                            DropdownMenuItem(text = { Text("Phim") }, onClick = { showMenu = false })
                            DropdownMenuItem(text = { Text("Ưu đãi") }, onClick = { showMenu = false })
                            DropdownMenuItem(text = { Text("Đăng nhập/đăng kí") }, onClick = { showMenu = false })
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        // 🔹 Thêm NavHost để điều hướng
        NavHost(navController = navController, startDestination = "home", Modifier.padding(innerPadding)) {
            composable("home") { HomeScreen(navController) } // 🔹 Điều hướng tới HomeScreen
// Điều hướng tới CardMovieScreen và truyền dữ liệu qua route
            composable(
                "card_movie/{imageRes}/{movieTitle}",
                arguments = listOf(
                    navArgument("imageRes") { type = NavType.IntType },
                    navArgument("movieTitle") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val imageRes = backStackEntry.arguments?.getInt("imageRes") ?: R.drawable.mat_biec
                val movieTitle = backStackEntry.arguments?.getString("movieTitle") ?: "Default Movie"

                // Gọi CardMovieScreen và truyền dữ liệu
                CardMovieScreen(navController,imageRes = imageRes, movieTitle = movieTitle)
            }        }
    }
}

// ✅ PreviewMyApp(): Xem trước giao diện trong Android Studio
@Preview(showBackground = true)
@Composable
fun PreviewMyApp() {
    MyApp()
}
