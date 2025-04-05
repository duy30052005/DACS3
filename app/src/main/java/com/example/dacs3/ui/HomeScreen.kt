package com.example.dacs3.ui


import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dacs3.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HomeScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    var showMenu by remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFA05F63))
    ) {
        val isLargeScreen = maxWidth > 600.dp // Xác định màn hình lớn (Tablet)
        val contentPadding = if (isLargeScreen) 24.dp else 0.dp // Điều chỉnh padding theo màn hình

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = contentPadding),
            verticalArrangement = Arrangement.spacedBy(if (isLargeScreen) 24.dp else 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isLargeScreen) 600.dp else 524.dp) // Điều chỉnh chiều cao Slideshow
            ) {
                AutoSlideshow(navController)
            }
            CustomDivider()

            // Hiển thị danh sách phim theo kiểu cột hoặc lưới
            MovieListSection(
                navController = navController,
                isLargeScreen = isLargeScreen
            )

            CustomDivider()
            PromotionScreen()
            CustomDivider()
        }
    }
}
// ✅ AutoSlideshow(): Hiệu ứng trình chiếu ảnh tự động
@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AutoSlideshow(navController: NavController) {
    val images = listOf(
        Pair(R.drawable.avengers, "Avengers: Endgame"),
        Pair(R.drawable.mat_biec, "Mắt Biếc"),
        Pair(R.drawable.thor, "Thor: Ragnarok")
    )

    val pagerState = rememberPagerState(pageCount = { images.size })
    var isScrolling by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }

    // Theo dõi trạng thái cuộn để ẩn/hiện nội dung
    LaunchedEffect(pagerState.isScrollInProgress) {
        isScrolling = pagerState.isScrollInProgress
        if (!isScrolling) {
            delay(500)
            showContent = true
        } else {
            showContent = false
        }
    }

    // Tự động chuyển trang
    LaunchedEffect(Unit) {
        repeat(Int.MAX_VALUE) {
            delay(5000)
            val nextPage = if (pagerState.currentPage == images.size - 1) 0 else pagerState.currentPage + 1
            pagerState.animateScrollToPage(nextPage, animationSpec = tween(durationMillis = 1500))
        }
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val screenHeight = maxHeight
        val imageHeight = if (screenHeight < 600.dp) 524.dp else 574.dp  // Màn hình lớn thì tăng chiều cao ảnh
        val textSize = if (screenHeight < 600.dp) 20.sp else 27.sp  // Font lớn hơn trên tablet
        val paddingBottom = if (screenHeight < 600.dp) 55.dp else 80.dp  // Điều chỉnh padding

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
        ) {
            val coroutineScope = rememberCoroutineScope()

            // Slideshow
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight)
                        .clickable{
                            navController.navigate("card_movie/${images[page].first}/${images[page].second}")
                        }
                ) {
                    Image(
                        painter = painterResource(id = images[page].first),
                        contentDescription = null,
                        modifier = Modifier
                            .height(imageHeight - 100.dp) // Giữ tỉ lệ ảnh phù hợp
                            .fillMaxWidth()
                            .drawWithContent {
                                drawContent()
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color(0xFFA05F63)),
                                        startY = size.height * 0.01f, // 60% ảnh bắt đầu làm mờ
                                        endY = size.height // 100% ảnh mờ hoàn toàn
                                    ),
                                    alpha = 1f // Điều chỉnh độ trong suốt
                                )
                            }
                            .background(Color.Gray),
                        contentScale = ContentScale.Crop,

                    )

                    // Hiệu ứng xuất hiện cho tên phim
                    AnimatedVisibility(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = paddingBottom)
                            .background(Color.Black.copy(alpha = 0f), shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        visible = showContent,
                        enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(
                            animationSpec = tween(1000),
                            initialOffsetY = { -100 }
                        )
                    ) {
                        Text(
                            text = images[page].second,
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall.copy(fontSize = textSize),
                        )
                    }

                    // Hiệu ứng xuất hiện cho các nút
                    AnimatedVisibility(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 10.dp),
                        visible = showContent,
                        enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(
                            animationSpec = tween(1000),
                            initialOffsetY = { 30 }
                        )
                    ) {
                        Row(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    navController.navigate("card_movie/${images[page].first}/${images[page].second}")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF952531))

                            ) {
                                Text("Xem chi tiết", fontSize = textSize)
                            }
                            Button(
                                onClick = {
                                    navController.navigate("movie_schedule/${images[page].first}/${images[page].second}")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF952531))
                            ) {
                                Text("Đặt vé", fontSize = textSize)
                            }
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MovieListSection(navController: NavController, isLargeScreen: Boolean = false) {
    var selectedTab by remember { mutableStateOf("Đang Chiếu") } // Trạng thái tab

    val nowShowingMovies = listOf(
        Pair(R.drawable.avengers, "Avengers: Endgame"),
        Pair(R.drawable.mat_biec, "Mắt Biếc"),
        Pair(R.drawable.thor, "Thor: Ragnarok"),
    )

    val upcomingMovies = listOf(
        Pair(R.drawable.trang_quynh, "Trạng Quỳnh"),
        Pair(R.drawable.us, "Us"),

    )

    val moviesToShow = if (selectedTab == "Đang Chiếu") nowShowingMovies else upcomingMovies

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔹 Thanh chọn tab phim
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            listOf("Đang Chiếu", "Sắp Chiếu").forEach { tab ->
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    onClick = { selectedTab = tab },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == tab) Color(0xFF952531) else Color.Transparent
                    )
                ) {
                    Text(
                        text = "PHIM $tab".uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        BoxWithConstraints {
            val isTablet = maxWidth > 600.dp

            if (isTablet) {
                // 🔹 Hiển thị dạng lưới trên màn hình lớn
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(180.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 620.dp), // 🔥 Giới hạn chiều cao,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(moviesToShow) { movie ->
                        MovieItem(navController, movie)
                    }
                }
            } else {
                // 🔹 Hiển thị dạng danh sách ngang trên màn hình nhỏ
                val listState = rememberLazyListState()
                val coroutineScope = rememberCoroutineScope()

                // ✅ Tự động cuộn khi đang hoạt động
                LaunchedEffect(selectedTab) {
                    while (isActive) {
                        val itemCount = moviesToShow.size
                        for (index in 0 until itemCount) {
                            listState.animateScrollToItem(index)
                            delay(5000) // Dừng 5 giây trước khi chuyển tiếp
                        }
                    }
                }

                LazyRow(
                    state = listState,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    items(moviesToShow) { movie ->
                        MovieItem(navController, movie)
                    }
                }
            }
        }
    }
}

// 🔹 Tách riêng phần hiển thị phim để tái sử dụng dễ dàng
@Composable
fun MovieItem(navController: NavController, movie: Pair<Int, String>) {
    Column(
        modifier = Modifier
            .width(180.dp)
            .clickable {
                navController.navigate("card_movie/${movie.first}/${movie.second}")
            }
            .padding(3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(movie.first),
            contentDescription = movie.second,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(220.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = movie.second,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PromotionScreen() {
    val promotionList = listOf(
        R.drawable.uu_dai_1,
        R.drawable.uu_dai_2,
        R.drawable.uu_dai_3,
        R.drawable.uu_dai_4
    ).take(4) // Chỉ lấy 4 phần tử đầu tiên

    Column(
        modifier = Modifier.fillMaxSize()

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // Tiêu đề KHÔNG cuộn theo danh sách
            Text(
                text = "KHUYẾN MÃI & ƯU ĐÃI",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(16.dp)

            )
        }
        Spacer(modifier = Modifier.height(8.dp)) // Khoảng cách với danh sách ảnh

        // Lưới khuyến mãi (Chỉ hiển thị 4 ảnh)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp) // Giới hạn chiều cao để tránh lỗi
                .padding(horizontal = 16.dp)
        ) {
            items(promotionList) { imageRes ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* Xử lý khi bấm */ }
                        .height(150.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp)) // Khoảng cách với nút bấm

        // Nút "XEM NHIỀU HƠN"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color(0xFF952531))
                .clickable { /* Xử lý khi bấm */ },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+ XEM NHIỀU HƠN",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
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

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    val navController = rememberNavController()

    HomeScreen(navController = navController)
}
