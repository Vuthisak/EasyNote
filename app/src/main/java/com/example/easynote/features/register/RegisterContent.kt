package com.example.easynote.features.register

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.easynote.R
import com.example.easynote.features.main.MainActivity
import com.example.easynote.features.register.state.RegisterState
import com.example.easynote.features.register.state.RegisterUiState
import com.example.easynote.ui.theme.buttonHeight
import com.example.easynote.util.Loading
import com.example.easynote.util.TextInputField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun RegisterContent(
    activity: RegisterActivity,
    viewModel: RegisterViewModel
) = Content(activity, viewModel)

@Composable
fun Content(
    activity: RegisterActivity,
    viewModel: RegisterViewModel
) {
    val context = LocalContext.current
    val registerUiState = RegisterUiState()

    handleState(activity, viewModel, registerUiState, context)

    Box(Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Red),
            topBar = {
                TopBar(activity = activity)
            }
        ) {
            Body(registerUiState = registerUiState) {
                viewModel.register(
                    registerUiState.usernameState.value, registerUiState.passwordState.value
                )
            }
        }
        Loading(loadingState = registerUiState.loadingState)
    }
}

@Composable
private fun handleState(
    activity: RegisterActivity,
    viewModel: RegisterViewModel,
    registerUiState: RegisterUiState,
    context: Context
) {
    activity.lifecycleScope.launch {
        viewModel.state.collectLatest { state ->
            when (state) {
                is RegisterState.Loading -> registerUiState.showLoading()
                is RegisterState.Finished -> registerUiState.hideLoading()
                is RegisterState.Error -> {
                    Toast.makeText(context, state.ex.message, Toast.LENGTH_SHORT).show()
                }
                is RegisterState.RegisteredSuccess -> {
                    val intent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    intent.flags = FLAG_ACTIVITY_CLEAR_TOP
                    context.startActivity(intent)
                }
            }
        }
    }
}

@Composable
private fun TopBar(activity: RegisterActivity) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.text_register)) },
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
fun Body(registerUiState: RegisterUiState, onRegisterClicked: () -> Unit) {
    val isEnabledButton =
        remember(
            registerUiState.passwordState.value,
            registerUiState.confirmPasswordState.value
        ) {
            registerUiState.passwordState.value.isNotEmpty() &&
                    registerUiState.confirmPasswordState.value.isNotEmpty() &&
                    registerUiState.passwordState.value == registerUiState.confirmPasswordState.value
        }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        UsernameTextField(usernameText = registerUiState.usernameState)

        Spacer(modifier = Modifier.height(8.dp))

        PasswordTextField(passwordText = registerUiState.passwordState)

        Spacer(modifier = Modifier.height(8.dp))

        ConfirmPasswordTextField(registerUiState)

        Spacer(modifier = Modifier.height(32.dp))

        RegisterButton(onRegisterClicked, isEnabledButton)

    }
}

@Composable
private fun ConfirmPasswordTextField(registerUiState: RegisterUiState) {
    PasswordTextField(
        passwordText = registerUiState.confirmPasswordState,
        labelText = stringResource(id = R.string.text_confirm_password)
    )
}

@Composable
private fun RegisterButton(onRegisterClicked: () -> Unit, isEnabledButton: Boolean) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(buttonHeight),
        onClick = onRegisterClicked,
        enabled = isEnabledButton
    ) {
        Text(text = stringResource(id = R.string.text_register))
    }
}

@Composable
private fun PasswordTextField(
    passwordText: MutableState<String>,
    labelText: String = stringResource(id = R.string.text_password)
) {
    val passwordVisible = rememberSaveable { mutableStateOf(false) }
    TextInputField(
        valueState = passwordText,
        labelText = labelText,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = getPasswordVisibleIcon(passwordVisible)
            val description = if (passwordVisible.value) "Hide password" else "Show password"
            IconButton(onClick = {
                passwordVisible.value = !passwordVisible.value
            }) {
                Icon(imageVector = image, description)
            }
        }
    )
}

@Composable
private fun getPasswordVisibleIcon(passwordVisible: MutableState<Boolean>) =
    if (passwordVisible.value) {
        Icons.Filled.Visibility
    } else {
        Icons.Filled.VisibilityOff
    }

@Composable
private fun UsernameTextField(usernameText: MutableState<String>) {
    TextInputField(
        valueState = usernameText,
        labelText = stringResource(id = R.string.text_username),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            imeAction = ImeAction.Next
        )
    )
}
