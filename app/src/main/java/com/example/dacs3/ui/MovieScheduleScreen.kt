package com.example.dacs3.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MovieScheduleScreen(navController: NavHostController, imageRes: Int, movieTitle: String) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dayFormat = SimpleDateFormat("dd", Locale.getDefault())
    val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault()) // Tháng + năm
    val weekDayFormat = SimpleDateFormat("EEE", Locale.getDefault()) // Thứ

    val calendar = Calendar.getInstance()
    val monthYear = remember { monthFormat.format(calendar.time) }
    val next7Days = remember {
        mutableStateListOf<Pair<String, String>>().apply {
            repeat(7) {
                val day = dayFormat.format(calendar.time)  // Ngày
                val weekDay = weekDayFormat.format(calendar.time)  // Thứ
                add(Pair(day, weekDay))
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
        }
    }

    val today = next7Days.first().first
    var selectedTab by remember { mutableStateOf(today) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .background(Color(0xFFA05F63)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = {
                /* Xử lý hành động trở về tại đây */
                navController.navigate("home")
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
        // Hiển thị tháng
        Text(
            text = monthYear.replaceFirstChar { it.uppercase() }, // Viết hoa chữ đầu
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Hiển thị ngày + thứ
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            next7Days.forEach { (day, weekDay) ->
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .background(
                                color = if (selectedTab == day) Color(0xFF952531) else Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedTab = day } // Thay thế Button bằng Box có sự kiện click
                            .padding(vertical = 12.dp), // Tạo padding để dễ bấm hơn
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day,
                            fontSize = if (selectedTab == day) 18.sp else 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == day) Color.White else Color.Black,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                    Text(text = weekDay, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hình ảnh phim
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = movieTitle,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFA05F63))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tên phim
        Text(
            text = movieTitle,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Lưới suất chiếu
        LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.fillMaxWidth()) {
            items(6) { index ->
                ShowtimeButton(
                    navController,
                    cinema = "Rạp $index",
                    startTime = "14:00",
                    endTime = "16:30",
                    imageRes,
                    movieTitle)
            }
        }
    }
}

@Composable
fun ShowtimeButton(navController: NavController,
                   cinema: String,
                   startTime: String,
                   endTime: String,
                   imageRes: Int,
                   movieTitle: String) {
    Button(
        onClick = {
            navController.navigate("cinema_seat/${imageRes}/${movieTitle}")

        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF952531)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = cinema, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = "$startTime - $endTime", fontSize = 12.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMovieScheduleScreen() {
    val navController = rememberNavController()
    MovieScheduleScreen(navController = navController, imageRes = R.drawable.avengers, movieTitle = "Avengers: Endgame")
}
