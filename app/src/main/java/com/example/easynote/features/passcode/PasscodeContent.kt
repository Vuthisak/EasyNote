package com.example.easynote.features.passcode

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easynote.R
import com.example.easynote.base.BaseContent
import com.example.easynote.features.main.MainActivity
import com.example.easynote.features.passcode.state.PasscodeUiState
import com.example.easynote.ui.theme.buttonHeight
import com.example.easynote.util.Loading
import com.example.easynote.util.TextInputPassword

class PasscodeContent(
    private val activity: PasscodeActivity,
    private val viewModel: PasscodeViewModel
) : BaseContent() {

    @Composable
    override fun register() {
        val uiState = remember { PasscodeUiState() }
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
            Loading(loadingState = uiState.loadingState)
        }
    }

    @Composable
    private fun TopBar() {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.text_passcode),
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            elevation = 0.dp,
            backgroundColor = Color.Transparent,
        )
    }

    @Composable
    private fun Body() {
        val uiState = PasscodeUiState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            PasscodeTextField(uiState = uiState)

            Spacer(modifier = Modifier.height(16.dp))

            NextButton(uiState = uiState)

        }
    }

    @Composable
    private fun PasscodeTextField(uiState: PasscodeUiState) {
        TextInputPassword(
            valueState = uiState.passcodeState,
            labelText = stringResource(id = R.string.text_passcode),
            maxLength = 6
        )
    }

    @Composable
    private fun NextButton(uiState: PasscodeUiState) {
        val isButtonEnabled = remember(uiState.passcodeState.value) {
            uiState.passcodeState.value.isNotEmpty()
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(buttonHeight),
            enabled = isButtonEnabled,
            onClick = {
                viewModel.savePasscode(uiState.passcodeState.value)
                gotoMainScreen()
            }) {
            Text(text = stringResource(id = R.string.action_next))
        }
    }

    private fun gotoMainScreen() {
        val intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

}
