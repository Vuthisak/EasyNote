package com.example.easynote.features.notedetail.state

sealed class NoteDetailState {
    object Finished : NoteDetailState()
    object Loading : NoteDetailState()
    object UpdateOrSaveSuccess : NoteDetailState()
    data class Error(val ex: Throwable) : NoteDetailState()
}