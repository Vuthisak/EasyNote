package com.example.easynote

import android.app.Application
import com.example.easynote.di.repository
import com.example.easynote.di.viewModel
import com.example.easynote.util.encryption.EncryptionManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class EasyNoteApp : Application() {

    override fun onCreate() {
        super.onCreate()
        EncryptionManager.initialize(this)
        startKoin {
            androidLogger()
            androidContext(this@EasyNoteApp)
            modules(modules = listOf(viewModel, repository))
        }
    }

}
