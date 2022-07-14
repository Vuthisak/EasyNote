package com.example.easynote.features.forgotpassword.request

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.easynote.R
import com.example.easynote.base.BaseContent
import com.example.easynote.features.forgotpassword.request.state.ForgotPasswordUiState
import com.example.easynote.util.Loading
import com.example.easynote.util.TextInputField

class ForgotPasswordContent(
    private val activity: ForgotPasswordActivity,
    private val viewModel: ForgotPasswordViewModel
) : BaseContent() {

    @Composable
    override fun register() {
        val uiState: ForgotPasswordUiState = ForgotPasswordUiState()

        Box(Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Red),
                topBar = { TopBar() }
            ) {
                Body(uiState)
                Loading(loadingState = uiState.loadingState)
            }
        }
    }

    @Composable
    private fun TopBar() {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.text_forgot_password)) },
            navigationIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .clickable { activity.finish() }
                )
            }
        )
    }

    @Composable
    fun Body(uiState: ForgotPasswordUiState) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            EmailTextField(uiState)
        }
    }

    @Composable
    private fun EmailTextField(uiState: ForgotPasswordUiState) {
        TextInputField(
            valueState = uiState.emailState,
            labelText = stringResource(id = R.string.text_email)
        )
    }


}