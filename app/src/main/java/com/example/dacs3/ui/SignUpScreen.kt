package com.example.dacs3.ui.theme

import androidx.compose.foundation.Image
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
import com.example.dacs3.Primary
import com.example.dacs3.TextColor
import com.example.dacs3.WhiteColor
import com.example.dacs3.GrayColor
import com.example.dacs3.R
import org.json.JSONObject

@Composable
fun SignUpScreen(
    navController: NavController,
    onSignUpSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    SignUpScreenUI(
        email = email,
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it },
        confirmPassword = confirmPassword,
        onConfirmPasswordChange = { confirmPassword = it },
        errorMessage = errorMessage,
        successMessage = successMessage,
        onRegisterClick = {
            when {
                email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                    errorMessage = "Vui lòng nhập đầy đủ thông tin"
                    successMessage = ""
                }
                !email.contains("@") || !email.contains(".") -> {
                    errorMessage = "Email không hợp lệ"
                    successMessage = ""
                }
                password.length < 6 -> {
                    errorMessage = "Mật khẩu phải có ít nhất 6 ký tự"
                    successMessage = ""
                }
                password != confirmPassword -> {
                    errorMessage = "Mật khẩu không khớp"
                    successMessage = ""
                }
                else -> {
                    errorMessage = ""
                    // Gửi dữ liệu đến API
                    val queue = Volley.newRequestQueue(navController.context)
                    val url = "http://192.168.1.4/api/save_user.php" // Đã cập nhật IP

                    val request = object : StringRequest(
                        Method.POST, url,
                        { response ->
                            println("API Response: $response") // In phản hồi để kiểm tra
                            try {
                                // Kiểm tra xem phản hồi có phải JSON không
                                if (response.trim().startsWith("{") || response.trim().startsWith("[")) {
                                    val jsonResponse = JSONObject(response)
                                    if (jsonResponse.has("message") && jsonResponse.getString("message") == "User saved successfully") {
                                        successMessage = "Đăng ký thành công!"
                                        errorMessage = ""
                                        onSignUpSuccess()
                                    } else if (jsonResponse.has("error") && jsonResponse.getString("error").contains("Email already exists")) {
                                        errorMessage = "Email đã tồn tại"
                                        successMessage = ""
                                    } else {
                                        errorMessage = "Đăng ký thất bại: ${jsonResponse.optString("error", response)}"
                                        successMessage = ""
                                    }
                                } else {
                                    errorMessage = "Phản hồi từ máy chủ không hợp lệ: $response"
                                    successMessage = ""
                                }
                            } catch (e: Exception) {
                                errorMessage = "Lỗi xử lý phản hồi: ${e.message}"
                                successMessage = ""
                            }
                        },
                        { error ->
                            errorMessage = "Lỗi kết nối: ${error.message}"
                            successMessage = ""
                        }
                    ) {
                        override fun getParams(): Map<String, String> {
                            val params = HashMap<String, String>()
                            params["email"] = email
                            params["password"] = password
                            params["ten"] = ""
                            params["diaChi"] = ""
                            params["soDienThoai"] = ""
                            params["ngaySinh"] = ""
                            params["avatar"] = ""
                            return params
                        }
                    }
                    queue.add(request)
                }
            }
        },
        onLoginClick = { onNavigateToLogin() }
    )
}

@Composable
fun SignUpScreenUI(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    errorMessage: String,
    successMessage: String,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
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
                NormalTextComponent(value = "Xin chào")
                HeadingTextComponent(value = "Tạo tài khoản mới")
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
                PasswordTextField(
                    labelValue = "Confirm Password",
                    leadingIcon = Icons.Default.Lock,
                    value = confirmPassword,
                    onValueChange = onConfirmPasswordChange
                )
                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = onRegisterClick,
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
                        text = "Đăng ký",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Bạn đã có tài khoản? ", color = TextColor)
                    Text(
                        text = "Đăng nhập",
                        color = Primary,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { onLoginClick() }
                    )
                }
                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (successMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = successMessage,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

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

@Preview(showBackground = true)
@Composable
fun DefaultSignUpScreenPreview() {
    SignUpScreenUI(
        email = "example@gmail.com",
        onEmailChange = {},
        password = "password123",
        onPasswordChange = {},
        confirmPassword = "password123",
        onConfirmPasswordChange = {},
        errorMessage = "",
        successMessage = "",
        onRegisterClick = {},
        onLoginClick = {}
    )
}