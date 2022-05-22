package com.example.easynote.features.notedetail

sealed class NoteDetailState {
    object Default : NoteDetailState()
    object Finished : NoteDetailState()
    object Loading : NoteDetailState()
    object UpdateOrSaveSuccess : NoteDetailState()
    data class Error(val ex: Throwable) : NoteDetailState()
}