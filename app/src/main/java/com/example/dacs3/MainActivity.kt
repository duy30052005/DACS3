package com.example.dacs3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.dacs3.R
import kotlin.random.Random
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.*



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    var showText by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ứng dụng của tôi") },
                actions = {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Tùy chọn 1") },
                                onClick = { showMenu = false } // Đóng menu khi chọn
                            )
                            DropdownMenuItem(
                                text = { Text("Tùy chọn 2") },
                                onClick = { showMenu = false }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Background()
            Text(
                text = "Hello Universe!",
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center)
                    .background(Color.White)

            )
        }
    }

}

@Composable
fun Background() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(color = Color.Black) // Nền đen vũ trụ

        // Vẽ 100 ngôi sao ngẫu nhiên
        repeat(1000) {
            val x = Random.nextFloat() * size.width
            val y = Random.nextFloat() * size.height
            val radius = Random.nextFloat() * 3f + 1f // Ngôi sao có kích thước từ 1 đến 4 px
            drawCircle(color = Color.White, radius = radius, center = androidx.compose.ui.geometry.Offset(x, y))
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewMyApp() {
    MyApp()
}