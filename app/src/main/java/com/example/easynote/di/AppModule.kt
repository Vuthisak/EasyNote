package com.example.easynote.di

import com.example.easynote.data.LocalPreferences
import com.example.easynote.data.LocalPreferencesImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single<LocalPreferences> {
        LocalPreferencesImpl(androidContext())
    }
}
