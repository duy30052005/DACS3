package com.example.dacs3.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithEdit(labelValue: String, leadingIcon: ImageVector, textValue: String, onTextChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = textValue,
        onValueChange = { onTextChange(it) },
        label = { Text(text = labelValue) },
        leadingIcon = {
            Icon(imageVector = leadingIcon, contentDescription = null)
        },

        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Primary,
            focusedLabelColor = Primary,
            cursorColor = Primary,
            containerColor = BgColor,
        ),
        keyboardOptions = KeyboardOptions.Default,
        textStyle = TextStyle(
            fontSize = 19.sp, // Tăng cỡ chữ
            fontWeight = FontWeight.SemiBold, // Làm đậm chữ
        )
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MailWithEdit(labelValue: String, leadingIcon: ImageVector, textValue: String, onTextChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = textValue,
        onValueChange = { onTextChange(it) },
        label = { Text(text = labelValue) },
        leadingIcon = {
            Icon(imageVector = leadingIcon, contentDescription = null)
        },
        trailingIcon = {
            IconButton(onClick = { /* Xử lý sự kiện chỉnh sửa */ }) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit")
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Primary,
            focusedLabelColor = Primary,
            cursorColor = Primary,
            containerColor = BgColor,
        ),
        keyboardOptions = KeyboardOptions.Default,
        textStyle = TextStyle(
            fontSize = 19.sp, // Tăng cỡ chữ
            fontWeight = FontWeight.SemiBold, // Làm đậm chữ
        )
    )
}

@Composable
fun Information() {
    // Lưu trữ giá trị mặc định cho các trường
    val firstName = remember { mutableStateOf("John") }
    val lastName = remember { mutableStateOf("Doe") }
    val email = remember { mutableStateOf("john.doe@example.com") }
    val password = remember { mutableStateOf("123456") }

    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HeadingTextComponent(value = "Account Information")
            Spacer(modifier = Modifier.height(20.dp))

            // Các TextField với thông tin sẵn có và icon chỉnh sửa
            TextFieldWithEdit(
                labelValue = "First Name",
                leadingIcon = Icons.Default.Person,
                textValue = firstName.value,
                onTextChange = { firstName.value = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextFieldWithEdit(
                labelValue = "Last Name",
                leadingIcon = Icons.Default.Person,
                textValue = lastName.value,
                onTextChange = { lastName.value = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            MailWithEdit(
                labelValue = "Email",
                leadingIcon = Icons.Default.Email,
                textValue = email.value,
                onTextChange = { email.value = it }
            )



        }
    }
}

@Preview
@Composable
fun DefaultInfor() {
    Information()
}
