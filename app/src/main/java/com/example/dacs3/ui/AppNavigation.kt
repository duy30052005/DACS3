package com.example.dacs3

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dacs3.ui.*
import com.example.dacs3.ui.theme.SignUpScreen
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

val componentShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(0.dp)
)

sealed class BottomNavItem(val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    object Movies : BottomNavItem("movies", Icons.Default.Movie, "Movies")
    object Chart : BottomNavItem("chart", Icons.Default.TrendingUp, "Chart")
    object User : BottomNavItem("user", Icons.Default.Person, "User")
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    selectedItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Movies,
        BottomNavItem.Chart,
        BottomNavItem.User
    )

    NavigationBar(
        containerColor = WhiteColor,
        contentColor = Primary
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = selectedItem == item,
                onClick = { onItemSelected(item) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Primary,
                    selectedTextColor = Primary,
                    unselectedIconColor = GrayColor,
                    unselectedTextColor = GrayColor
                )
            )
        }
    }
}

@Composable
fun NavigationScreen(navController: NavController, userId: String) {
    var selectedItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it }
            )
        }
    ) { innerPadding ->
        val bottomPadding = innerPadding.calculateBottomPadding()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = bottomPadding)
        ) {
            when (selectedItem) {
                BottomNavItem.Home -> HomeScreen(navController)
                BottomNavItem.Movies -> AllMoviesScreen(navController)
                BottomNavItem.Chart -> PlaceholderScreen("Chart Screen - Coming Soon")
                BottomNavItem.User -> UserProfileScreen(
                    navController = navController,
                    userId = userId,
                    onProfileSaved = { selectedItem = BottomNavItem.Home }
                )
            }
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextColor
            )
        )
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var userId by remember { mutableStateOf("") }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                navController = navController,
                onLoginSuccess = { id ->
                    userId = id
                    navController.navigate("home") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                onNavigateToSignUp = { navController.navigate("signup") }
            )
        }
        composable("signup") {
            SignUpScreen(
                navController = navController,
                onSignUpSuccess = { navController.navigate("login") },
                onNavigateToLogin = { navController.navigate("login") }
            )
        }
        composable("user_profile") {
            UserProfileScreen(
                navController = navController,
                userId = userId,
                onProfileSaved = {
                    navController.navigate("home") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            NavigationScreen(navController = navController, userId = userId)
        }
        composable(
            "card_movie/{imageRes}/{movieTitle}/{videoId}",
            arguments = listOf(
                navArgument("imageRes") { type = NavType.IntType },
                navArgument("movieTitle") { type = NavType.StringType },
                navArgument("videoId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val imageRes = backStackEntry.arguments?.getInt("imageRes") ?: R.drawable.mat_biec
            val movieTitle = backStackEntry.arguments?.getString("movieTitle") ?: "Default Movie"
            val videoId = backStackEntry.arguments?.getString("videoId") ?: ""
            CardMovieScreen(navController, imageRes = imageRes, movieTitle = movieTitle, videoId = videoId)
        }
        composable(
            "movie_schedule/{imageRes}/{movieTitle}",
            arguments = listOf(
                navArgument("imageRes") { type = NavType.IntType },
                navArgument("movieTitle") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val imageRes = backStackEntry.arguments?.getInt("imageRes") ?: R.drawable.mat_biec
            val movieTitle = backStackEntry.arguments?.getString("movieTitle") ?: "Default Movie"
            MovieScheduleScreen(navController, imageRes = imageRes, movieTitle = movieTitle)
        }
        composable(
            "cinema_seat/{imageRes}/{movieTitle}",
            arguments = listOf(
                navArgument("imageRes") { type = NavType.IntType },
                navArgument("movieTitle") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val imageRes = backStackEntry.arguments?.getInt("imageRes") ?: R.drawable.mat_biec
            val movieTitle = backStackEntry.arguments?.getString("movieTitle") ?: "Default Movie"
            CinemaSeatScreen(navController, imageRes = imageRes, movieTitle = movieTitle)
        }
    }
}