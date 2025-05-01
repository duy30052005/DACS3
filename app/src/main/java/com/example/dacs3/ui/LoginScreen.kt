package com.example.dacs3

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

val Primary = Color(0xFF92A3FD)
val Secondary = Color(0xFF9DCEFF)
val TextColor = Color(0xFF1D1617)
val AccentColor = Color(0xFFFC58BF)
val GrayColor = Color(0xFF7B6F72)
val WhiteColor = Color(0xFFFFFFFF)
val BgColor = Color(0xFFF7F8F8)

// Các component con
@Composable
fun NormalTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier.fillMaxWidth(),
        style = TextStyle(fontSize = 18.sp, color = GrayColor, fontWeight = FontWeight.Normal),
        textAlign = TextAlign.Center
    )
}

@Composable
fun HeadingTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier.fillMaxWidth(),
        style = TextStyle(fontSize = 30.sp, color = TextColor, fontWeight = FontWeight.Bold),
        textAlign = TextAlign.Center
    )
}

@Composable
fun MyTextField(
    labelValue: String,
    leadingIcon: ImageVector,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(labelValue) },
        leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun PasswordTextField(labelValue: String, leadingIcon: ImageVector, value: String, onValueChange: (String) -> Unit) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(labelValue) },
        leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = null) },
        trailingIcon = {
            val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = icon, contentDescription = null)
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun ForgotPasswordText(onClick: () -> Unit) {
    Text(
        text = "Bạn quên mật khẩu?",
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp).clickable { onClick() },
        style = TextStyle(fontSize = 14.sp, color = GrayColor, textDecoration = TextDecoration.Underline),
        textAlign = TextAlign.Center
    )
}

@Composable
fun DontHaveAccountText(onRegisterClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Chưa có tài khoản? ", color = TextColor)
        Text(
            text = "Đăng ký",
            color = Primary,
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable { onRegisterClick() }
        )
    }
}

@Composable
fun LoginScreenUI(
    email: String,
    password: String,
    errorMessage: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onForgotPasswordClick: () -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Surface(
        color = WhiteColor,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Hình nền
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Nội dung chính
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NormalTextComponent(value = "Cotte Cinema,")
                HeadingTextComponent(value = "Chào mừng trở lại")
                Spacer(modifier = Modifier.height(20.dp))

                MyTextField(
                    labelValue = "Email",
                    leadingIcon = Icons.Default.Email,
                    value = email,
                    onValueChange = onEmailChange,
                    keyboardType = KeyboardType.Email
                )
                PasswordTextField(
                    labelValue = "Password",
                    leadingIcon = Icons.Default.Lock,
                    value = password,
                    onValueChange = onPasswordChange
                )
                Spacer(modifier = Modifier.height(5.dp))

                ForgotPasswordText(onClick = onForgotPasswordClick)
                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = WhiteColor
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Text(
                        text = "Đăng nhập",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                DontHaveAccountText(onRegisterClick = onRegisterClick)

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    navController: NavController,
    onLoginSuccess: (String) -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    LoginScreenUI(
        email = email,
        password = password,
        errorMessage = errorMessage,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onForgotPasswordClick = { errorMessage = "Chức năng quên mật khẩu chưa được triển khai" },
        onLoginClick = {
            when {
                email.isEmpty() || password.isEmpty() -> {
                    errorMessage = "Vui lòng nhập email và mật khẩu"
                }
                !email.contains("@") || !email.contains(".") -> {
                    errorMessage = "Email không hợp lệ"
                }
                password.length < 6 -> {
                    errorMessage = "Mật khẩu phải có ít nhất 6 ký tự"
                }
                else -> {
                    errorMessage = ""
                    // Gửi dữ liệu đến API để đăng nhập
                    val queue = Volley.newRequestQueue(navController.context)
                    val url = "http://192.168.1.4/api/login_user.php" // Thay bằng IP của bạn

                    val request = object : StringRequest(
                        Method.POST, url,
                        { response ->
                            try {
                                val jsonResponse = JSONObject(response)
                                if (jsonResponse.has("message") && jsonResponse.getString("message") == "Login successful") {
                                    val userId = jsonResponse.getString("userId")
                                    errorMessage = ""
                                    onLoginSuccess(userId)
                                } else {
                                    errorMessage = "Email hoặc mật khẩu không đúng"
                                }
                            } catch (e: Exception) {
                                errorMessage = "Lỗi xử lý phản hồi: ${e.message}"
                            }
                        },
                        { error ->
                            errorMessage = "Lỗi kết nối: ${error.message}"
                        }
                    ) {
                        override fun getParams(): Map<String, String> {
                            val params = HashMap<String, String>()
                            params["email"] = email
                            params["password"] = password
                            return params
                        }
                    }
                    queue.add(request)
                }
            }
        },
        onRegisterClick = onNavigateToSignUp
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreenUI() {
    Surface(modifier = Modifier.fillMaxSize()) {
        LoginScreenUI(
            email = "",
            password = "",
            errorMessage = "",
            onEmailChange = {},
            onPasswordChange = {},
            onForgotPasswordClick = {},
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}