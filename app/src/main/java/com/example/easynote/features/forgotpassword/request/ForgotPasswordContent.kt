package com.example.easynote.features.forgotpassword.request

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.easynote.R
import com.example.easynote.base.BaseContent
import com.example.easynote.features.forgotpassword.request.state.ForgotPasswordState
import com.example.easynote.features.forgotpassword.request.state.ForgotPasswordUiState
import com.example.easynote.features.forgotpassword.resetpassword.ResetPasswordActivity
import com.example.easynote.ui.theme.buttonHeight
import com.example.easynote.util.ArrowBackIcon
import com.example.easynote.util.Loading
import com.example.easynote.util.TextInputField
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ForgotPasswordContent(
    private val activity: ForgotPasswordActivity,
    private val viewModel: ForgotPasswordViewModel
) : BaseContent() {

    private var isSuccess: Boolean = false

    @Composable
    override fun register() {
        val uiState = ForgotPasswordUiState()
        handleState(uiState)

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

    private fun handleState(uiState: ForgotPasswordUiState) {
        activity.lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                when (state) {
                    is ForgotPasswordState.Loading -> uiState.showLoading()
                    is ForgotPasswordState.Finished -> uiState.hideLoading()
                    is ForgotPasswordState.Success -> gotoConfirmCodeScreen()
                    is ForgotPasswordState.Error -> onError(state.ex)
                }
            }
        }
    }

    private fun gotoConfirmCodeScreen() {
        isSuccess = true
        val intent = Intent(activity, ResetPasswordActivity::class.java)
        activity.startActivity(intent)
    }

    private fun onError(ex: Throwable) {
        Toast.makeText(activity, ex.message, Toast.LENGTH_SHORT).show()
    }

    @Composable
    private fun TopBar() {
        TopAppBar(
            title = { ForgotPasswordText() },
            navigationIcon = { ArrowBackIcon { activity.finish() } }
        )
    }

    @Composable
    private fun ForgotPasswordText() {
        Text(text = stringResource(id = R.string.title_forgot_password))
    }

    @Composable
    private fun Body(uiState: ForgotPasswordUiState) {
        val isButtonEnabled = remember(uiState.emailState.value) {
            uiState.emailState.value.isNotEmpty()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            EmailTextField(uiState)

            Spacer(modifier = Modifier.height(16.dp))

            RequestButton(uiState.emailState, isButtonEnabled)
        }
    }

    @Composable
    private fun EmailTextField(uiState: ForgotPasswordUiState) {
        TextInputField(
            valueState = uiState.emailState,
            labelText = stringResource(id = R.string.text_email)
        )
    }

    @Composable
    private fun RequestButton(emailState: MutableState<String>, isEnabled: Boolean) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(buttonHeight),
            onClick = {
                if (isSuccess) {
                    gotoConfirmCodeScreen()
                } else {
                    FirebaseAuth.getInstance().signOut()
                    viewModel.requestForgotPassword(emailState.value)
                }
            },
            enabled = isEnabled
        ) {
            Text(text = stringResource(id = R.string.action_request))
        }
    }

}