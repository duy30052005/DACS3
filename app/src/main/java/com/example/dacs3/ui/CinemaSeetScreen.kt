package com.example.dacs3.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Path
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dacs3.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.io.path.Path
import kotlin.io.path.moveTo

@Composable
fun CinemaSeatScreen(navController: NavHostController, imageRes: Int, movieTitle: String) {
    val rows = listOf("A", "B", "C", "D", "E", "F", "G")
    val columns = (1..9).toList()
    val selectedSeats = remember { mutableStateListOf<String>() }
    val bookedSeats = listOf("D02", "D03", "E05", "F02", "H07")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent, Color.Black
                    )

                )
            )


    ) {
        Image(
            painter = painterResource(id = imageRes) ,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = size.height * 1f, // 60% ảnh bắt đầu làm mờ
                            endY = size.height // 100% ảnh mờ hoàn toàn
                        ),
                        alpha = 0.7f // Điều chỉnh độ trong suốt
                    )
                }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            IconButton(
                onClick = {
                    /* Xử lý hành động trở về tại đây */
                    navController.navigate("movie_schedule/${imageRes}/${movieTitle}")
                          },

                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Start) // Đặt iconbutton nằm bên trái

                    .clip(RoundedCornerShape(50.dp)),
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack, // Biểu tượng mũi tên trở về
                    contentDescription = "Trở về", // Mô tả cho người sử dụng hỗ trợ
                    tint = Color.White // Màu của biểu tượng (có thể thay đổi)
                )
            }
            // Nội dung của bạn ở đây
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                val path = Path().apply {
                    moveTo(0f, size.height)
                    quadraticBezierTo(
                        size.width / 2, -20f,
                        size.width, size.height)
                }
                drawPath(
                    path,
                    color = Color.White,
                    style = Stroke(width = 4.dp.toPx()))
            }
            Text(
                text = "Màn hình",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyVerticalGrid(columns = GridCells.Fixed(columns.size)) {
                items(rows.flatMap { row -> columns.map { col -> "$row$col" } }) { seat ->
                    val isBooked = seat in bookedSeats
                    val isSelected = seat in selectedSeats

                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(30.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                when {
                                    isBooked -> Color.DarkGray
                                    isSelected -> Color.Red
                                    else -> Color.Gray
                                }
                            )
                            .clickable(enabled = !isBooked) {
                                if (isSelected) selectedSeats.remove(seat)
                                else selectedSeats.add(seat)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = seat, color = Color.White, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val seatPrice = 70000
            val totalPrice = selectedSeats.size * seatPrice
            Row(verticalAlignment = Alignment.CenterVertically) {
                LegendItem(color = Color.Gray, label = "Ghế tiêu chuẩn")
                LegendItem(color = Color.Red, label = "Ghế đang chọn")
                LegendItem(color = Color.DarkGray, label = "Ghế đã đặt")
            }
            // Danh sách ghế đã chọn
            Row(
                modifier = Modifier
                    .fillMaxWidth()

                    .padding(20.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ghế đã chọn: ",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (selectedSeats.isEmpty()) "Chưa chọn ghế nào" else selectedSeats.joinToString(", "),
                    color = Color.Red,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()

                    .padding(20.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tổng tiền: ",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${totalPrice.toString()} VND",
                    color = Color.Red,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()

                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier
                        .weight(1f),
                    onClick = {
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF952531))
                ) {
                    Text("Hoàn tất", fontSize = 16.sp)
                }
            }


        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, shape = RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, color = Color.White, fontSize = 16.sp)
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewCinemaSeet() {
    val navController = rememberNavController()
    CinemaSeatScreen(navController = navController, imageRes = R.drawable.avengers, movieTitle = "Avengers: Endgame")

}
