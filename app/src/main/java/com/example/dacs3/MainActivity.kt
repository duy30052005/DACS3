package com.example.dacs3

// ✅ Nhóm 1: Android Core (Các thành phần cơ bản của Android)
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically

// ✅ Nhóm 2: Compose UI (Các thành phần giao diện trong Jetpack Compose)
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// ✅ Nhóm 3: Material Icons (Các biểu tượng trong Material Design)
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings

// ✅ Nhóm 4: Jetpack Compose Theme (Hỗ trợ light mode & dark mode)
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.draw.clip

// ✅ Nhóm 5: Hỗ trợ hình ảnh từ URL bằng thư viện Coil
import coil.compose.AsyncImage

// ✅ Nhóm 6: Hệ thống vẽ & Random (Hỗ trợ vẽ và tạo số ngẫu nhiên)
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.*
import kotlin.random.Random

// ✅ Nhóm 7: Resources (Truy cập tài nguyên trong thư mục res)
import com.example.dacs3.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
    var showMenu by remember { mutableStateOf(false) } // Trạng thái của menu dropdown
    var showMenu1 by remember { mutableStateOf(false) } // Trạng thái của menu dropdown

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Tên rạp phim") }, // Tiêu đề của thanh công cụ
                actions = {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu") // Nút mở menu
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false } // Đóng menu khi nhấn ra ngoài
                        ) {
                            DropdownMenuItem(
                                text = { Text("Lịch chiếu") },
                                onClick = { showMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Phim") },
                                onClick = { showMenu = false }
                            )
                            DropdownMenuItem(
                                    text = { Text("Ưu đãi") },
                            onClick = { showMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Đăng nhập/đăng kí") },
                                onClick = { showMenu = false }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column( // Sắp xếp AutoSlideshow và CustomDivider theo chiều dọc
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF120018)) // Màu nền
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp), // Sử dụng khoảng cách thay vì dàn đều
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box( // ✅ Bọc AutoSlideshow() trong Box có chiều cao 250.dp
                modifier = Modifier
                    .fillMaxWidth()
                    .height(424.dp) // Giới hạn chiều cao
            ) {
                AutoSlideshow()
            }
            CustomDivider(modifier = Modifier.padding(innerPadding))  // Thanh ngang ở dưới

        }
    }
}

// ✅ AutoSlideshow(): Hiệu ứng trình chiếu ảnh tự động
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AutoSlideshow() {
    val images = listOf(
        Pair(R.drawable.avengers, "Avengers: Endgame"),
        Pair(R.drawable.mat_biec, "Mắt Biếc"),
        Pair(R.drawable.thor, "Thor: Ragnarok")
    )

    val pagerState = rememberPagerState(pageCount = { images.size })
    var isScrolling by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) } // Trạng thái để kiểm soát hiệu ứng xuất hiện

    // Theo dõi trạng thái cuộn để ẩn/hiện nội dung
    LaunchedEffect(pagerState.isScrollInProgress) {
        isScrolling = pagerState.isScrollInProgress
        if (!isScrolling) {
            delay(500) // Chờ 0.5 giây trước khi hiển thị nội dung
            showContent = true
        } else {
            showContent = false
        }
    }

    // Tự động chuyển trang
    LaunchedEffect(Unit) {
        repeat(Int.MAX_VALUE) {
            delay(5000) // Chờ 5 giây
            val nextPage = if (pagerState.currentPage == images.size - 1) 0 else pagerState.currentPage + 1
            pagerState.animateScrollToPage(nextPage, animationSpec = tween(durationMillis = 1500))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(524.dp)
    ) {
        val coroutineScope = rememberCoroutineScope()

        // Slideshow chính
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(524.dp)
            ) {
                // Hiển thị ảnh
                Image(
                    painter = painterResource(id = images[page].first),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()

                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )

                // Hiệu ứng xuất hiện cho tên phim
                AnimatedVisibility(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp)
                        .background(Color.Black.copy(alpha = 0.6f), shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    visible = showContent,
                    enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(
                        animationSpec = tween(1000),
                        initialOffsetY = { -100 } // Trượt xuống từ trên cùng

                    )
                ) {
                    Text(
                        text = images[page].second,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
//
                    )
                }

                // Hiệu ứng xuất hiện cho các nút
                AnimatedVisibility(
                    modifier = Modifier

                        .align(Alignment.BottomCenter)
                        .padding(bottom = 10.dp), // Điều chỉnh vị trí xuống dưới
                    visible = showContent,
                    enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(
                        animationSpec = tween(1000),
                        initialOffsetY = { 30 } // Trượt lên từ dưới
                    )
                ) {
                    Row(
                        modifier = Modifier

                            .align(Alignment.BottomCenter),
//                            .padding(bottom = 40.dp), // Điều chỉnh vị trí xuống dưới
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(onClick = { /* Xử lý xem chi tiết */ }) {
                            Text("Xem chi tiết")
                        }
                        Button(
                            onClick = { /* Xử lý thêm vào danh sách */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B2CBF))
                        ) {
                            Text("Thêm vào danh sách")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CustomDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth() // Chiếm toàn bộ màn hình để có thể căn giữa
            .padding(vertical = 16.dp), // Tạo khoảng cách với các phần tử khác
        contentAlignment = Alignment.Center // Căn giữa cả chiều ngang và dọc
    ) {
        Box(
            modifier = Modifier
                .width(300.dp)     // Độ rộng có thể điều chỉnh
                .height(5.dp) // Độ dày có thể điều chỉnh
                .background(Color.White, shape = RoundedCornerShape(50)) // Màu trắng, bo góc nhẹ
        )
    }
}


// ✅ PreviewMyApp(): Xem trước giao diện trong Android Studio
@Preview(showBackground = true)
@Composable
fun PreviewMyApp() {
    MyApp()
}
