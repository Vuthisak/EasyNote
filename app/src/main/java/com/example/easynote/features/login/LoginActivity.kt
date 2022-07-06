package com.example.easynote.features.login

import androidx.compose.runtime.Composable
import com.example.easynote.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity() {

    private val viewModel: LoginViewModel by viewModel()

    @Composable
    override fun Content() = LoginContent(this, viewModel)

}
