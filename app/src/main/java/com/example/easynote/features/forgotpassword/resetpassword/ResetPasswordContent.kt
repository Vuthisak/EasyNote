package com.example.easynote.features.forgotpassword.resetpassword

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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.easynote.R
import com.example.easynote.base.BaseContent
import com.example.easynote.features.forgotpassword.resetpassword.state.ResetPasswordState
import com.example.easynote.features.forgotpassword.resetpassword.state.ResetPasswordUiState
import com.example.easynote.features.login.LoginActivity
import com.example.easynote.ui.theme.buttonHeight
import com.example.easynote.util.ArrowBackIcon
import com.example.easynote.util.Loading
import com.example.easynote.util.TextInputField
import com.example.easynote.util.TextInputPassword
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ResetPasswordContent(
    private val activity: ResetPasswordActivity,
    private val viewModel: ResetPasswordViewModel
) : BaseContent() {

    @Composable
    override fun register() {
        val uiState = ResetPasswordUiState()
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

    private fun handleState(uiState: ResetPasswordUiState) {
        activity.lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                when (state) {
                    is ResetPasswordState.Loading -> uiState.showLoading()
                    is ResetPasswordState.Finished -> uiState.hideLoading()
                    is ResetPasswordState.Error -> onError(state.ex)
                    is ResetPasswordState.Success -> gotoLoginScreen()
                }
            }
        }
    }

    private fun onError(ex: Throwable) {
        Toast.makeText(activity, ex.message, Toast.LENGTH_SHORT).show()
    }

    private fun gotoLoginScreen() {
        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent)
    }

    @Composable
    private fun TopBar() {
        TopAppBar(
            title = { ResetPasswordText() },
            navigationIcon = { ArrowBackIcon { activity.finish() } }
        )
    }

    @Composable
    private fun ResetPasswordText() {
        Text(text = stringResource(id = R.string.text_reset_password))
    }

    @Composable
    private fun Body(uiState: ResetPasswordUiState) {
        val isButtonEnabled = uiState.run {
            remember(
                codeState.value,
                newPasswordState.value,
                confirmNewPasswordState.value
            ) {
                codeState.value.isNotEmpty()
                        && newPasswordState.value.isNotEmpty()
                        && confirmNewPasswordState.value.isNotEmpty()
                        && newPasswordState.value == confirmNewPasswordState.value
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            CodeTextField(uiState.codeState)

            Spacer(modifier = Modifier.height(8.dp))

            NewPasswordTextField(uiState.newPasswordState)

            Spacer(modifier = Modifier.height(8.dp))

            ConfirmNewPasswordTextField(uiState.confirmNewPasswordState)

            Spacer(modifier = Modifier.height(16.dp))

            ConfirmButton(uiState, isButtonEnabled)
        }
    }

    @Composable
    private fun CodeTextField(codeState: MutableState<String>) {
        TextInputField(
            valueState = codeState,
            labelText = stringResource(id = R.string.text_code)
        )
    }

    @Composable
    private fun NewPasswordTextField(newPasswordState: MutableState<String>) {
        TextInputPassword(
            valueState = newPasswordState,
            labelText = stringResource(id = R.string.text_new_password),
            imeAction = ImeAction.Next
        )
    }

    @Composable
    private fun ConfirmNewPasswordTextField(confirmNewPasswordState: MutableState<String>) {
        TextInputPassword(
            valueState = confirmNewPasswordState,
            labelText = stringResource(id = R.string.text_confirm_new_password),
            imeAction = ImeAction.Done
        )
    }

    @Composable
    private fun ConfirmButton(uiState: ResetPasswordUiState, isEnabled: Boolean) {
        Button(
            modifier = Modifier
                .height(buttonHeight)
                .fillMaxWidth(),
            enabled = isEnabled,
            onClick = {
                uiState.run { viewModel.resetPassword(codeState.value, newPasswordState.value) }
            }
        ) {
            Text(text = stringResource(id = R.string.action_confirm))
        }
    }

}
