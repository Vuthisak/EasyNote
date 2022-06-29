package com.example.easynote.features.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easynote.R

@Composable
fun LoginContent() {
    Box(Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {

            },
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Red)
        ) {
            Body(it)
        }
    }
}

@Composable
private fun Body(it: PaddingValues) {
    val usernameText = remember {
        mutableStateOf("")
    }
    val passwordText = remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(it),
        verticalArrangement = Arrangement.Center
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Image(
                modifier = Modifier.size(size = 140.dp),
                painter = painterResource(id = R.drawable.img_notes),
                contentDescription = "logo app"
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .wrapContentHeight(),
            value = usernameText.value,
            onValueChange = { usernameText.value = it },
            label = { Text(text = stringResource(id = R.string.text_username)) },
            maxLines = 1,
            colors = defaultTextFieldColors(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .wrapContentHeight(),
            value = passwordText.value,
            onValueChange = { passwordText.value = it },
            label = { Text(text = stringResource(id = R.string.text_password)) },
            maxLines = 1,
            colors = defaultTextFieldColors(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp),
            text = "Forgot password?",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.End
            ),
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(24.dp))
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            Button(modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
                onClick = {

                }
            ) {
                Text(text = stringResource(id = R.string.action_login))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = "Or create account?",
            style = TextStyle(
                fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Center
            ),
            color = Color.Gray
        )
    }
}

@Composable
private fun defaultTextFieldColors() = TextFieldDefaults.textFieldColors(
    backgroundColor = Color.Transparent
)