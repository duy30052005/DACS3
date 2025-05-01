package com.example.dacs3.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.dacs3.Primary
import com.example.dacs3.TextColor
import com.example.dacs3.WhiteColor
import com.example.dacs3.GrayColor
import com.example.dacs3.R
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    navController: NavController,
    userId: String,
    onProfileSaved: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    var avatarBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var avatarUrl by remember { mutableStateOf<String?>(null) }

    // Trạng thái cho tab
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Thông tin", "Đổi mật khẩu")

    // Trạng thái cho tab đổi mật khẩu
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }

    // Date picker state
    var showDatePicker by remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()

    // Launcher để chọn ảnh từ thư viện
    val context = navController.context
    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            avatarUri = it
            try {
                val inputStream = context.contentResolver.openInputStream(avatarUri!!)
                avatarBitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
            } catch (e: Exception) {
                errorMessage = "Lỗi khi tải ảnh: ${e.message}"
            }
        }
    }

    // Lấy thông tin người dùng khi màn hình được tải
    LaunchedEffect(userId) {
        val queue = Volley.newRequestQueue(navController.context)
        val url = "http://192.168.1.4/api/get_user.php?userId=$userId"

        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                println("API Response: $response")
                try {
                    if (response.trim().startsWith("{") || response.trim().startsWith("[")) {
                        val jsonResponse = JSONObject(response)
                        if (!jsonResponse.has("error")) {
                            email = jsonResponse.getString("Email")
                            name = jsonResponse.getString("Ten")
                            address = jsonResponse.getString("DiaChi")
                            phoneNumber = jsonResponse.getString("SoDienThoai")
                            dateOfBirth = jsonResponse.getString("NgaySinh")
                            avatarUrl = jsonResponse.optString("Avatar", null)
                            println("Avatar URL: $avatarUrl")
                        } else {
                            errorMessage = jsonResponse.optString("error", "Không tìm thấy người dùng")
                        }
                    } else {
                        errorMessage = "Phản hồi từ máy chủ không hợp lệ: $response"
                    }
                } catch (e: Exception) {
                    errorMessage = "Lỗi xử lý dữ liệu: ${e.message}"
                }
            },
            { error ->
                errorMessage = "Lỗi kết nối: ${error.message}"
            }
        )
        queue.add(request)
    }

    Surface(
        color = WhiteColor,
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars) // Đảm bảo nội dung không bị che bởi status bar
                .padding(horizontal = 28.dp), // Chỉ padding ngang, không padding đỉnh
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Thanh tab
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth(),
                containerColor = WhiteColor,
                contentColor = Primary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)) },
                        modifier = Modifier.background(WhiteColor)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nội dung của tab
            when (selectedTab) {
                0 -> { // Tab "Thông tin"
                    UserProfileScreenUI(
                        name = name,
                        onNameChange = { name = it },
                        onAvatarClick = { pickImageLauncher.launch("image/*") },
                        email = email,
                        onEmailChange = { email = it }, // Không dùng vì trường Email cố định
                        address = address,
                        onAddressChange = { address = it },
                        phoneNumber = phoneNumber,
                        onPhoneNumberChange = { phoneNumber = it },
                        dateOfBirth = dateOfBirth,
                        onDateOfBirthClick = { showDatePicker = true },
                        avatarBitmap = avatarBitmap,
                        avatarUrl = avatarUrl,
                        errorMessage = errorMessage,
                        successMessage = successMessage,
                        onSaveClick = {
                            when {
                                name.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || dateOfBirth.isEmpty() -> {
                                    errorMessage = "Vui lòng nhập đầy đủ thông tin"
                                    successMessage = ""
                                }
                                phoneNumber.length < 10 -> {
                                    errorMessage = "Số điện thoại không hợp lệ"
                                    successMessage = ""
                                }
                                else -> {
                                    errorMessage = ""
                                    val queue = Volley.newRequestQueue(navController.context)
                                    val url = "http://192.168.1.4/api/update_user.php"

                                    val request = object : StringRequest(
                                        Method.POST, url,
                                        { response ->
                                            println("API Response: $response")
                                            try {
                                                if (response.trim().startsWith("{") || response.trim().startsWith("[")) {
                                                    val jsonResponse = JSONObject(response)
                                                    if (jsonResponse.has("message") && jsonResponse.getString("message") == "User updated successfully") {
                                                        successMessage = "Cập nhật thông tin thành công!"
                                                        errorMessage = ""
                                                        avatarBitmap = null
                                                        avatarUrl = "uploads/avatar_${userId}_${System.currentTimeMillis()}.jpg"
                                                        onProfileSaved()
                                                    } else {
                                                        errorMessage = jsonResponse.optString("error", "Cập nhật thông tin thất bại: $response")
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
                                            params["userId"] = userId
                                            params["email"] = email
                                            params["ten"] = name
                                            params["diaChi"] = address
                                            params["soDienThoai"] = phoneNumber
                                            params["ngaySinh"] = dateOfBirth
                                            val bitmap = avatarBitmap
                                            if (bitmap != null) {
                                                val byteArrayOutputStream = ByteArrayOutputStream()
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                                                val byteArray = byteArrayOutputStream.toByteArray()
                                                val base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT)
                                                params["avatar"] = base64Image
                                            } else {
                                                params["avatar"] = ""
                                            }
                                            return params
                                        }
                                    }
                                    queue.add(request)
                                }
                            }
                        }
                    )
                }
                1 -> { // Tab "Đổi mật khẩu"
                    ChangePasswordUI(
                        oldPassword = oldPassword,
                        onOldPasswordChange = { oldPassword = it },
                        newPassword = newPassword,
                        onNewPasswordChange = { newPassword = it },
                        confirmNewPassword = confirmNewPassword,
                        onConfirmNewPasswordChange = { confirmNewPassword = it },
                        errorMessage = errorMessage,
                        successMessage = successMessage,
                        onSaveClick = {
                            when {
                                oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty() -> {
                                    errorMessage = "Vui lòng nhập đầy đủ thông tin"
                                    successMessage = ""
                                }
                                newPassword != confirmNewPassword -> {
                                    errorMessage = "Mật khẩu mới và xác nhận không khớp"
                                    successMessage = ""
                                }
                                newPassword.length < 6 -> {
                                    errorMessage = "Mật khẩu mới phải có ít nhất 6 ký tự"
                                    successMessage = ""
                                }
                                else -> {
                                    errorMessage = ""
                                    val queue = Volley.newRequestQueue(navController.context)
                                    val url = "http://192.168.1.4/api/update_password.php"

                                    val request = object : StringRequest(
                                        Method.POST, url,
                                        { response ->
                                            println("API Response: $response")
                                            try {
                                                if (response.trim().startsWith("{") || response.trim().startsWith("[")) {
                                                    val jsonResponse = JSONObject(response)
                                                    if (jsonResponse.has("message") && jsonResponse.getString("message") == "Password updated successfully") {
                                                        successMessage = "Đổi mật khẩu thành công!"
                                                        errorMessage = ""
                                                        oldPassword = ""
                                                        newPassword = ""
                                                        confirmNewPassword = ""
                                                    } else {
                                                        errorMessage = jsonResponse.optString("error", "Đổi mật khẩu thất bại: $response")
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
                                            params["userId"] = userId
                                            params["oldPassword"] = oldPassword
                                            params["newPassword"] = newPassword
                                            return params
                                        }
                                    }
                                    queue.add(request)
                                }
                            }
                        }
                    )
                }
            }

            // Date picker dialog (chỉ dùng trong tab Thông tin)
            if (showDatePicker && selectedTab == 0) {
                val datePickerState = rememberDatePickerState()
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    calendar.timeInMillis = millis
                                    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                    dateOfBirth = formatter.format(calendar.time)
                                }
                                showDatePicker = false
                            }
                        ) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }
    }
}

@Composable
fun UserProfileScreenUI(
    name: String,
    onNameChange: (String) -> Unit,
    onAvatarClick: () -> Unit,
    email: String,
    onEmailChange: (String) -> Unit, // Không dùng vì cố định
    address: String,
    onAddressChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    dateOfBirth: String,
    onDateOfBirthClick: () -> Unit,
    avatarBitmap: Bitmap?,
    avatarUrl: String?,
    errorMessage: String,
    successMessage: String,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NormalTextComponent(value = "Xin chào,")
        HeadingTextComponent(value = "Thông tin tài khoản")
        Spacer(modifier = Modifier.height(20.dp))

        // Thành phần avatar
        Box {
            when {
                avatarBitmap != null -> {
                    Image(
                        bitmap = avatarBitmap.asImageBitmap(),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                avatarUrl != null && avatarUrl.isNotEmpty() -> {
                    AsyncImage(
                        model = "http://192.168.1.4/api/$avatarUrl",
                        contentDescription = "Avatar from Server",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.mat_biec),
                        placeholder = painterResource(id = R.drawable.mat_biec)
                    )
                }
                else -> {
                    Image(
                        painter = painterResource(id = R.drawable.mat_biec),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            IconButton(
                onClick = onAvatarClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(36.dp)
                    .offset(x = 8.dp, y = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Change Avatar",
                    tint = GrayColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        MyTextField(
            labelValue = "Full Name",
            leadingIcon = Icons.Default.Person,
            value = name,
            onValueChange = onNameChange,
            keyboardType = KeyboardType.Text
        )
        // Trường Email cố định
        OutlinedTextField(
            value = email,
            onValueChange = { /* Read-only */ },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            enabled = false, // Cố định, không cho chỉnh sửa
            shape = RoundedCornerShape(12.dp)
        )
        MyTextField(
            labelValue = "Address",
            leadingIcon = Icons.Default.LocationOn,
            value = address,
            onValueChange = onAddressChange,
            keyboardType = KeyboardType.Text
        )
        MyTextField(
            labelValue = "Phone Number",
            leadingIcon = Icons.Default.Phone,
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            keyboardType = KeyboardType.Phone
        )
        OutlinedTextField(
            value = dateOfBirth,
            onValueChange = { /* Read-only */ },
            label = { Text("Date of Birth") },
            leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { onDateOfBirthClick() },
            enabled = false,
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onSaveClick,
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
                text = "Save",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
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

@Composable
fun ChangePasswordUI(
    oldPassword: String,
    onOldPasswordChange: (String) -> Unit,
    newPassword: String,
    onNewPasswordChange: (String) -> Unit,
    confirmNewPassword: String,
    onConfirmNewPasswordChange: (String) -> Unit,
    errorMessage: String,
    successMessage: String,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NormalTextComponent(value = "Xin chào,")
        HeadingTextComponent(value = "Đổi mật khẩu")
        Spacer(modifier = Modifier.height(20.dp))

        // Đồng bộ giao diện với tab Thông tin
        MyTextField(
            labelValue = "Mật khẩu cũ",
            leadingIcon = Icons.Default.Lock,
            value = oldPassword,
            onValueChange = onOldPasswordChange,
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )
        MyTextField(
            labelValue = "Mật khẩu mới",
            leadingIcon = Icons.Default.Lock,
            value = newPassword,
            onValueChange = onNewPasswordChange,
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )
        MyTextField(
            labelValue = "Xác nhận mật khẩu mới",
            leadingIcon = Icons.Default.Lock,
            value = confirmNewPassword,
            onValueChange = onConfirmNewPasswordChange,
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onSaveClick,
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
                text = "Lưu",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
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
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None
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
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(12.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun UserProfileScreenPreview() {
    UserProfileScreenUI(
        name = "Nguyen Van A",
        onNameChange = {},
        onAvatarClick = {},
        email = "nguyenvana@example.com",
        onEmailChange = {},
        address = "123 Hanoi",
        onAddressChange = {},
        phoneNumber = "0123456789",
        onPhoneNumberChange = {},
        dateOfBirth = "01/01/2000",
        onDateOfBirthClick = {},
        avatarBitmap = null,
        avatarUrl = null,
        errorMessage = "",
        successMessage = "",
        onSaveClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ChangePasswordUIPreview() {
    ChangePasswordUI(
        oldPassword = "",
        onOldPasswordChange = {},
        newPassword = "",
        onNewPasswordChange = {},
        confirmNewPassword = "",
        onConfirmNewPasswordChange = {},
        errorMessage = "",
        successMessage = "",
        onSaveClick = {}
    )
}