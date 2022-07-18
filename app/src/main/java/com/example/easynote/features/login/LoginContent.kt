package com.example.easynote.features.login

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.easynote.R
import com.example.easynote.base.BaseContent
import com.example.easynote.features.forgotpassword.request.ForgotPasswordActivity
import com.example.easynote.features.login.listener.LoginListener
import com.example.easynote.features.login.state.LoginState
import com.example.easynote.features.login.state.LoginUiState
import com.example.easynote.features.passcode.PasscodeActivity
import com.example.easynote.features.register.RegisterActivity
import com.example.easynote.ui.theme.buttonHeight
import com.example.easynote.util.Loading
import com.example.easynote.util.TextInputField
import com.example.easynote.util.TextInputPassword
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginContent(
    private val activity: LoginActivity,
    private val viewModel: LoginViewModel
) : BaseContent() {

    @Composable
    override fun register() {
        val loginState = remember { LoginUiState() }
        handleState(loginState)

        Box(Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Red)
            ) {
                Body(loginState, getLoginListener())
                Loading(loadingState = loginState.loadingState)
            }
        }
    }


    @Composable
    private fun handleState(loginState: LoginUiState) {
        activity.lifecycleScope.launch {
            viewModel.state.collectLatest {
                when (it) {
                    is LoginState.Loading -> loginState.showLoading()
                    is LoginState.Finished -> loginState.hideLoading()
                    is LoginState.Error -> onError(it.error)
                    is LoginState.Success -> gotoMainScreen()
                }
            }
        }
    }

    private fun onError(ex: Throwable) {
        Toast.makeText(activity, ex.message, Toast.LENGTH_SHORT).show()
    }

    private fun gotoMainScreen() {
        with(activity) {
            val intent = Intent(this, PasscodeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }

    @Composable
    private fun Body(loginUiState: LoginUiState, listener: LoginListener) {
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

                ForgotPasswordText(listener)

                Spacer(modifier = Modifier.height(24.dp))

                LoginButton(listener, loginUiState)

                Spacer(modifier = Modifier.height(16.dp))

                CreateAccountText(listener)
            }
        }
    }

    @Composable
    private fun getLoginListener(): LoginListener {
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

            override fun onForgotPasswordClicked() {
                Intent(context, ForgotPasswordActivity::class.java).apply {
                    context.startActivity(this)
                }
            }
        }
    }

    @Composable
    private fun CreateAccountText(listener: LoginListener) {
        Text(
            modifier = Modifier
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
    private fun LoginButton(listener: LoginListener, loginState: LoginUiState) {
        with(loginState) {
            Button(modifier = Modifier
                .fillMaxWidth()
                .height(buttonHeight),
                onClick = {
                    listener.onLoginClicked(usernameState.value.trim(), passwordState.value)
                }
            ) {
                Text(text = stringResource(id = R.string.action_login))
            }
        }
    }

    @Composable
    private fun ForgotPasswordText(listener: LoginListener) {
        Text(
            modifier = Modifier
                .padding(end = 16.dp)
                .clickable { listener.onForgotPasswordClicked() },
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
        TextInputPassword(
            valueState = passwordText,
            labelText = stringResource(id = R.string.text_password)
        )
    }

    @Composable
    private fun UsernameTextField(usernameText: MutableState<String>) {
        TextInputField(
            valueState = usernameText,
            labelText = stringResource(id = R.string.text_email),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Next
            )
        )
    }

}
