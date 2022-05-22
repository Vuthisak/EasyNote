package com.example.easynote.di

import com.example.easynote.repository.NoteRepository
import com.example.easynote.repository.NoteRepositoryImpl
import org.koin.dsl.module

val repository = module {
    factory<NoteRepository> {
        NoteRepositoryImpl()
    }
}