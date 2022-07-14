package com.example.easynote.features.login

import com.example.easynote.base.BaseActivity
import com.example.easynote.base.BaseContent
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity() {

    private val viewModel: LoginViewModel by viewModel()
    override val content: BaseContent by lazy(LazyThreadSafetyMode.NONE) {
        LoginContent(this, viewModel)
    }

}
