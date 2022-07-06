package com.example.easynote.features.login

import android.content.Intent
import androidx.compose.runtime.Composable
import com.example.easynote.base.BaseActivity
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
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

}
