package com.example.easynote.features.login

import android.content.Intent
import androidx.compose.runtime.Composable
import com.example.easynote.BaseActivity
import com.example.easynote.features.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity() {

    private val viewModel: LoginViewModel by viewModel()

    @Composable
    override fun Content() {
        LoginContent(
            viewModel,
            onSuccess = {
                gotoMainScreen()
            }, onError = {
                showToast(it.message.toString())
            }
        )
    }

    private fun gotoMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
