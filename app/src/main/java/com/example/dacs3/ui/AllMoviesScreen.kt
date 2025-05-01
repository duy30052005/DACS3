package com.example.dacs3.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dacs3.R

// Data class để đại diện cho một phim
data class Movie(
    val imageRes: Int,
    val title: String,
    val category: String,
    val rating: Int // Điểm đánh giá (3, 4, 5 sao)
)

@Composable
fun AllMoviesScreen(navController: NavController, modifier: Modifier = Modifier) {
    // Danh sách phim mẫu
    val allMovies = listOf(
        Movie(R.drawable.avengers, "Avengers: Endgame", "Hành Động", 4),
        Movie(R.drawable.mat_biec, "Mắt Biếc", "Tình Cảm", 5),
        Movie(R.drawable.avengers, "The Conjuring", "Kinh Dị", 4),
        Movie(R.drawable.avengers, "Deadpool", "Hài", 3),
        Movie(R.drawable.avengers, "Avengers: Endgame", "Hành Động", 4),
        Movie(R.drawable.mat_biec, "Mắt Biếc", "Tình Cảm", 5),
        Movie(R.drawable.avengers, "The Conjuring", "Kinh Dị", 4),
        Movie(R.drawable.avengers, "Deadpool", "Hài", 3),
        Movie(R.drawable.avengers, "Avengers: Endgame", "Hành Động", 4),
        Movie(R.drawable.mat_biec, "Mắt Biếc", "Tình Cảm", 5),
        Movie(R.drawable.avengers, "The Conjuring", "Kinh Dị", 4),
        Movie(R.drawable.avengers, "Deadpool", "Hài", 3),
        Movie(R.drawable.avengers, "Avengers: Endgame", "Hành Động", 4),
        Movie(R.drawable.mat_biec, "Mắt Biếc", "Tình Cảm", 5),
        Movie(R.drawable.avengers, "The Conjuring", "Kinh Dị", 4),
        Movie(R.drawable.avengers, "Deadpool", "Hài", 3),
    )

    // Trạng thái để theo dõi thể loại được chọn
    var selectedCategory by remember { mutableStateOf("Tất Cả") }

    // Lọc danh sách phim theo thể loại
    val filteredMovies = if (selectedCategory == "Tất Cả") {
        allMovies
    } else {
        allMovies.filter { it.category == selectedCategory }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFA05F63)) // Màu nền nâu đỏ nhạt
    ) {
        // Thanh tiêu đề với dropdown menu
        FilterHeader(
            selectedCategory = selectedCategory,
            onCategorySelected = { category -> selectedCategory = category }
        )

        // Danh sách phim dạng lưới
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 cột
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredMovies) { movie ->
                MovieItem(movie = movie)
            }
        }
    }
}

@Composable
fun FilterHeader(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }

    // Biểu tượng mũi tên thay đổi theo trạng thái expanded
    val icon = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
    ) {
        OutlinedTextField(
            value = selectedCategory,
            onValueChange = { /* Không cho phép chỉnh sửa trực tiếp */ },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            label = { Text("Thể Loại", color = Color.White) },
            textStyle = TextStyle(color = Color.White),
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = "Dropdown",
                    tint = Color.White,
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                cursorColor = Color.White
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                .background(Color.White, RoundedCornerShape(8.dp))
        ) {
            listOf("Tất Cả", "Kinh Dị", "Tình Cảm", "Hành Động", "Hài").forEach { category ->
                DropdownMenuItem(
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    },
                    text = { // Sử dụng tham số text thay vì content
                        Text(text = category, color = Color.Black)
                    }
                )
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Xử lý khi nhấn vào phim */ },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF9E6) // Màu nền thẻ: kem nhạt để nổi bật trên nền nâu đỏ
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Hình ảnh phim
            Image(
                painter = painterResource(id = movie.imageRes),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tên phim
            Text(
                text = movie.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black, // Màu đen để nổi bật trên nền kem nhạt
                maxLines = 2,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Thể loại
            Text(
                text = movie.category,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFA726) // Màu cam nhạt để nổi bật
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Đánh giá sao
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(movie.rating) { // Hiển thị số sao tương ứng với rating
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = Color(0xFFFFD700), // Màu vàng cho sao
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    repeat(5 - movie.rating) { // Hiển thị sao trống
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Empty Star",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAllMoviesScreen() {
    val navController = rememberNavController()
    AllMoviesScreen(navController = navController)
}