package com.example.dacs3.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.dacs3.R
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MovieScheduleScreen() {
    val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
    val calendar = Calendar.getInstance()
    val next7Days = remember {
        mutableStateListOf<String>().apply {
            val calendar = Calendar.getInstance()
            repeat(7) {
                add(dateFormat.format(calendar.time))
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
        }
    }
    val today = next7Days.first()
    var selectedTab = remember { mutableStateOf(today) }

    Column(modifier = Modifier.fillMaxSize().padding(1.dp)) {
        // Row 1: Ngày
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            next7Days.forEach { date ->
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(2.dp),
                    onClick = { selectedTab.value = date },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab.value == date) Color(0xFF952531) else Color(0xFFA05F63),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        modifier = Modifier
                            .wrapContentWidth(),  // Thay đổi ở đây
                        text = date,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }


        Spacer(
            modifier = Modifier
                .height(16.dp)
        )

        // Row 2: Ảnh phim
        Image(
            painter = painterResource(id = R.drawable.us), // Thay bằng ID ảnh phim
            contentDescription = "Movie Poster",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Gray)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Row 3: Tên phim
        Text(
            text = "Tên Phim",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Row 4: Button suất chiếu dạng lưới
        LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.fillMaxWidth()) {
            items(6) { index ->
                ShowtimeButton(cinema = "Rạp $index", startTime = "14:00", endTime = "16:30")
            }
        }
    }
}

@Composable
fun ShowtimeButton(cinema: String, startTime: String, endTime: String) {
    Button(
        onClick = { /* Xử lý đặt vé */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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

    MovieScheduleScreen()
}
