package com.example.easynote.features.register

import androidx.compose.runtime.Composable
import com.example.easynote.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : BaseActivity() {

    private val viewModel: RegisterViewModel by viewModel()

    @Composable
    override fun Content() {
        RegisterContent(viewModel = viewModel)
    }

}