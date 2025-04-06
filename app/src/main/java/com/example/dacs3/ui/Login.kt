package com.example.dacs3.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

@Composable
fun ForgotPasswordText(onClick: () -> Unit) {
    Text(
        text = "Forgot your password?",
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .clickable { onClick() },
        style = TextStyle(
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline
        ),
        textAlign = TextAlign.Center
    )
}
@Composable
fun LoginButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Primary, // Màu nền nút
            contentColor = Color.White // Màu chữ
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Text(
            text = "Login",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}
@Composable
fun DontHaveAccountText(
    onRegisterClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Don't have an account? ", color = Color.Black)
        Text(
            text = "Register",
            color = Primary,
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable { onRegisterClick() }
        )
    }
}



@Composable
fun Login() {
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        Column (modifier = Modifier.fillMaxSize()){
            NormalTextComponent(value = "Hey there,")
            HeadingTextComponent(value = "Welcome Back")
            Spacer(modifier = Modifier.height(20.dp))

            MyTextField(
                labelValue = "Email",
                leadingIcon = Icons.Default.Email
            )
            PasswordTextField(
                labelValue = "Password",
                leadingIcon = Icons.Default.Lock
            )
            Spacer(modifier = Modifier.height(5.dp))
            ForgotPasswordText {
            }
            Spacer(modifier = Modifier.height(60.dp))

            LoginButton {
                Log.d("LoginButton", "Login clicked")
            }
            DontHaveAccountText {

            }

        }
    }
}

@Preview
@Composable
fun DefaultLogin() {
    Login()
}