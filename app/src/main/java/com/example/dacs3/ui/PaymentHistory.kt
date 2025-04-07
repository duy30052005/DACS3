package com.example.dacs3.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dacs3.R

@Composable
fun NormalTextPayment(value: String) {
    val textColor = Color.White // Thay đổi màu sắc theo chủ đề ứng dụng
    val fontSize = 28.sp // Tăng kích thước văn bản
    val fontWeight = FontWeight.SemiBold // Sử dụng độ đậm hơn
    val shadowElevation = 4.dp // Thêm bóng đổ

    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp) // Tăng chiều cao tối thiểu
            .padding(16.dp), // Thêm padding
        style = TextStyle(
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontStyle = FontStyle.Normal,
            color = textColor
        ),
        textAlign = TextAlign.Center
    )
}

@Composable
fun MovieItem(
    tenPhim: String,
    ngayDat: String,
    hinhAnhPhim: Painter
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Thẻ Card chứa ảnh phim
        Card(
            modifier = Modifier
                .size(120.dp) // Điều chỉnh kích thước thẻ card cho ảnh
                .padding(4.dp), // Khoảng cách trong thẻ card
            shape = RoundedCornerShape(8.dp), // Bo tròn các góc của thẻ
        ) {
            Image(
                painter = hinhAnhPhim,
                contentDescription = "Hình ảnh phim $tenPhim",
                modifier = Modifier.fillMaxSize(), // Đảm bảo ảnh chiếm hết không gian trong card
                contentScale = ContentScale.Crop // Làm cho ảnh to hơn và cắt phần thừa nếu cần
            )
        }

        Spacer(modifier = Modifier.width(16.dp)) // Khoảng cách giữa ảnh và thông tin phim

        // Column chứa tên phim và ngày đặt
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = tenPhim,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = ngayDat,
                fontSize = 14.sp
            )
        }

        // Icon mắt
        Icon(
            imageVector = Icons.Filled.Visibility,
            contentDescription = "Xem chi tiết",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun PaymentHistory() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Đặt hình nền vào Box
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        // LazyColumn hiển thị các mục
        LazyColumn(
            modifier = Modifier
                .padding(28.dp)
                .fillMaxWidth()
        ) {
            item {
                NormalTextPayment(value = "Payment History")
            }

            items(8) { index -> // Sử dụng items để hiển thị các mục trong danh sách
                MovieItem(
                    tenPhim = "Thor: Love and Thunder",
                    ngayDat = "15/07/2022",
                    hinhAnhPhim = when (index % 3) {
                        0 -> painterResource(id = R.drawable.thor)
                        1 -> painterResource(id = R.drawable.mat_biec)
                        else -> painterResource(id = R.drawable.trang_quynh)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun DefaultPayment() {
    PaymentHistory()
}

