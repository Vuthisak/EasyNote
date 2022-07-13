package com.example.easynote.features.passcode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

class PasswordContent(
    private val activity: PasscodeActivity,
    private val viewModel: PasscodeViewModel
) {

    @Composable
    fun get() {
        Box(Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Red),
                topBar = {
                    TopBar()
                }
            ) {
                Body()
            }
            // Loading(loadingState = registerUiState.loadingState)
        }
    }

    @Composable
    private fun Body() {
        Column(modifier = Modifier.fillMaxWidth()) {

        }
    }

    @Composable
    private fun TopBar() {

    }

}