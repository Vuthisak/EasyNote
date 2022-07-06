package com.example.easynote.features.login

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.easynote.R
import com.example.easynote.features.login.listener.LoginListener
import com.example.easynote.features.login.state.LoginState
import com.example.easynote.features.login.state.LoginUiState
import com.example.easynote.features.register.RegisterActivity
import com.example.easynote.ui.theme.buttonHeight
import com.example.easynote.util.Loading
import com.example.easynote.util.TextInputField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun LoginContent(
    viewModel: LoginViewModel,
    onSuccess: () -> Unit,
    onError: (ex: Throwable) -> Unit
) {
    val loginState = remember { LoginUiState() }

    handleState(viewModel, loginState, onSuccess, onError)

    Box(Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Red)
        ) {
            Body(loginState, getLoginListener(viewModel))
            Loading(loadingState = loginState.loadingState)
        }
    }
}

@Composable
private fun getLoginListener(viewModel: LoginViewModel): LoginListener {
    val context = LocalContext.current
    return object : LoginListener {
        override fun onLoginClicked(username: String, password: String) {
            viewModel.login(username, password)
        }

        override fun onCreateAccountClicked() {
            Intent(context, RegisterActivity::class.java).apply {
                context.startActivity(this)
            }
        }
    }
}

@Composable
private fun handleState(
    viewModel: LoginViewModel,
    loginState: LoginUiState,
    onSuccess: () -> Unit,
    onError: (ex: Throwable) -> Unit
) {
    viewModel.viewModelScope.launch(Dispatchers.Main) {
        viewModel.state.collectLatest {
            when (it) {
                is LoginState.Loading -> loginState.loadingState.value = true
                is LoginState.Finished -> loginState.loadingState.value = false
                is LoginState.Error -> onError(it.error)
                is LoginState.Success -> onSuccess()
            }
        }
    }
}

@Composable
private fun Body(
    loginUiState: LoginUiState,
    listener: LoginListener
) {
    with(loginUiState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            AppLogo()

            Spacer(modifier = Modifier.height(24.dp))

            UsernameTextField(usernameState)

            Spacer(modifier = Modifier.height(8.dp))

            PasswordTextField(passwordState)

            Spacer(modifier = Modifier.height(16.dp))

            ForgotPasswordText()

            Spacer(modifier = Modifier.height(24.dp))

            LoginButton(listener, usernameState, passwordState)

            Spacer(modifier = Modifier.height(16.dp))

            CreateAccountText(listener)
        }
    }
}

@Composable
private fun CreateAccountText(listener: LoginListener) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { listener.onCreateAccountClicked() },
        text = stringResource(id = R.string.text_create_account),
        style = TextStyle(
            fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Center
        ),
        color = Color.Gray
    )
}

@Composable
private fun LoginButton(
    listener: LoginListener,
    usernameText: MutableState<String>,
    passwordText: MutableState<String>
) {
    Button(modifier = Modifier
        .fillMaxWidth()
        .height(buttonHeight),
        onClick = {
            listener.onLoginClicked(usernameText.value, passwordText.value)
        }
    ) {
        Text(text = stringResource(id = R.string.action_login))
    }
}

@Composable
private fun ForgotPasswordText() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp),
        text = stringResource(id = R.string.text_forgot_password),
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.End
        ),
        color = Color.Gray
    )
}

@Composable
private fun AppLogo() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Image(
            modifier = Modifier.size(size = 140.dp),
            painter = painterResource(id = R.drawable.img_notes),
            contentDescription = "logo app"
        )
    }
}

@Composable
private fun PasswordTextField(passwordText: MutableState<String>) {
    val passwordVisible = rememberSaveable {
        mutableStateOf(false)
    }
    TextInputField(
        valueState = passwordText,
        labelText = stringResource(id = R.string.text_password),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image =
                if (passwordVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

            // Please provide localized description for accessibility services
            val description = if (passwordVisible.value) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Icon(imageVector = image, description)
            }
        }
    )
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
