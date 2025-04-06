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
import androidx.compose.ui.text.withStyle


val Primary = Color(0xFF92A3FD)
val Secondary = Color(0xFF9DCEFF)
val TextColor = Color(0xFF1D1617)
val AccentColor = Color(0xFFFC58BF)
val GrayColor = Color(0xFF7B6F72)
val WhiteColor = Color(0xFFFFFFFF)
val BgColor = Color(0xFFF7F8F8)
val componentShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(0.dp)
)
@Composable
fun NormalTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier.fillMaxWidth().heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ),
        color = Color(0xFF1D1617),
        textAlign = TextAlign.Center
    )
}
@Composable
fun HeadingTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier.fillMaxWidth().heightIn(),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ),
        color = Color(0xFF1D1617),
        textAlign = TextAlign.Center
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(labelValue: String, leadingIcon: ImageVector) {
    val textValue = remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = textValue.value,
        onValueChange = { textValue.value = it },
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
        keyboardOptions = KeyboardOptions.Default
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(labelValue: String, leadingIcon: ImageVector) {
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = password.value,
        onValueChange = { password.value = it },
        label = { Text(text = labelValue) },
        leadingIcon = {
            Icon(imageVector = leadingIcon, contentDescription = null)
        },
        trailingIcon = {
            val iconImage = if (passwordVisible.value) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            val description = if (passwordVisible.value) "Hide password" else "Show password"

            IconButton(onClick = {
                passwordVisible.value = !passwordVisible.value
            }) {
                Icon(imageVector = iconImage, contentDescription = description)
            }
        },
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Primary,
            focusedLabelColor = Primary,
            cursorColor = Primary,
            containerColor = BgColor,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}
@Composable
fun Checkbox(value: String) {
    val checkedState = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(56.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkedState.value,
            onCheckedChange = {
                checkedState.value = it  // ✅ Gán lại giá trị đúng
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        ClickableTextComponent(value = value)
    }
}

@Composable
fun ClickableTextComponent(value: String) {
    val initialText = "By continuing you accept our "
    val privacyPolicyText = "Privacy Policy"
    val andText = " and "
    val termsAndConditionsText = "Term of Use"

    val annotatedString = buildAnnotatedString {
        append(initialText)

        // Privacy Policy (clickable)
        pushStringAnnotation(tag = "privacy", annotation = "privacy")
        withStyle(style = SpanStyle(color = Primary)) {
            append(privacyPolicyText)
        }
        pop()

        append(andText)

        // Terms of Use (clickable)
        pushStringAnnotation(tag = "terms", annotation = "terms")
        withStyle(style = SpanStyle(color = Primary)) {
            append(termsAndConditionsText)
        }
        pop()
    }

    ClickableText(
        text = annotatedString,
        style = TextStyle(color = TextColor),
        onClick = { offset ->
            annotatedString.getStringAnnotations(start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    when (annotation.tag) {
                        "privacy" -> Log.d("ClickableText", "Clicked Privacy Policy")
                        "terms" -> Log.d("ClickableText", "Clicked Terms of Use")
                    }
                }
        }
    )
}
@Composable
fun RegisterButton(onClick: () -> Unit) {
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
            text = "Register",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun AlreadyHaveAccountText(
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔸 Divider with "or"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Divider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = Color.Gray
            )

            Text(
                text = "  or  ",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Divider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = Color.Gray
            )
        }

        // 🔹 Text with clickable "Login"
        Row {
            Text(text = "Already have an account? ", color = Color.Black)
            Text(
                text = "Login",
                color = Primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onLoginClick() }
            )
        }
    }
}


@Composable
fun Register() {
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
       Column (modifier = Modifier.fillMaxSize()){
           NormalTextComponent(value = "Hey there,")
           HeadingTextComponent(value = "Create an Account")
           Spacer(modifier = Modifier.height(20.dp))
           MyTextField(
               labelValue = "First Name",
               leadingIcon = Icons.Default.Person
           )
           MyTextField(
               labelValue = "Last Name",
               leadingIcon = Icons.Default.Person
           )
           MyTextField(
               labelValue = "Email",
               leadingIcon = Icons.Default.Email
           )
           PasswordTextField(
               labelValue = "Password",
               leadingIcon = Icons.Default.Lock
           )
           Checkbox(value = "")
           Spacer(modifier = Modifier.height(20.dp))
           RegisterButton {
               Log.d("RegisterButton", "Register clicked")
           }
           AlreadyHaveAccountText(
               onLoginClick = {
               }
           )
       }
    }
}



@Preview
@Composable
fun DefaultPreviewOfSignUpScreen() {
    Register()
}