package com.example.easynote.di

import com.example.easynote.features.notedetail.NoteDetailViewModel
import com.example.easynote.features.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModel = module {
    viewModel {
        MainViewModel(get())
    }

    viewModel {
        NoteDetailViewModel(get())
    }
}
