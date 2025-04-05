package com.example.dacs3.ui

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dacs3.MyApp
import com.example.dacs3.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive


@Composable
fun CardMovieScreen(navController: NavHostController, imageRes: Int, movieTitle: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFA05F63))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Cho phép cuộn xuống ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = movieTitle,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Gray),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = movieTitle,
            modifier = Modifier.padding(top = 8.dp),
            fontSize = 20.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text ="thời lượng: 119m",
            modifier = Modifier.padding(top = 8.dp),
            fontSize = 20.sp,
            color = Color.White,

            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

//        Text(
//            text = movieTitle,
//            modifier = Modifier.padding(top = 8.dp),
//            fontSize = 20.sp,
//            color = Color.White,
//
//            fontWeight = FontWeight.Bold
//        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                },
                modifier= Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF952531))

            ) {
                Text("Đánh giá", fontSize = 16.sp)
            }
            Button(
                onClick = {
                    navController.navigate("movie_schedule/${imageRes}/${movieTitle}")

                },
                modifier= Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF952531))
            ) {
                Text("Đặt vé", fontSize = 16.sp)
            }
        }
        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF952531))

        ) {
            Text(text = "Go to Home Screen")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewCardMovie() {
    val navController = rememberNavController()
    CardMovieScreen(navController = navController, imageRes = R.drawable.avengers, movieTitle = "Avengers: Endgame")
}