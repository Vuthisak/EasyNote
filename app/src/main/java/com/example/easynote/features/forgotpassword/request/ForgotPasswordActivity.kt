package com.example.easynote.features.forgotpassword.request

import com.example.easynote.base.BaseActivity
import com.example.easynote.base.BaseContent
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPasswordActivity : BaseActivity() {

    private val viewModel: ForgotPasswordViewModel by viewModel()
    override val content: BaseContent by lazy(LazyThreadSafetyMode.NONE) {
        ForgotPasswordContent(this, viewModel)
    }

}
