package com.example.easynote.features.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.easynote.features.register.state.RegisterUiState
import com.example.easynote.util.Loading

@Composable
fun RegisterContent(viewModel: RegisterViewModel) {
    Content()
}

@Composable
fun Content() {
    val registerUiState = RegisterUiState()
    Box(Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Red)
        ) {
            Body()
            Loading(loadingState = registerUiState.loadingState)
        }
    }
}

@Composable
fun Body() {

}
