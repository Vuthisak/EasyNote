package com.example.easynote.features.register

import com.example.easynote.base.BaseActivity
import com.example.easynote.base.BaseContent
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : BaseActivity() {

    private val viewModel: RegisterViewModel by viewModel()
    override val content: BaseContent by lazy(LazyThreadSafetyMode.NONE) {
        RegisterContent(activity = this, viewModel = viewModel)
    }

}
