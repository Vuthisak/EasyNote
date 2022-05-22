package com.example.easynote.features.notedetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easynote.entity.Note
import com.example.easynote.repository.NoteRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteDetailViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    private var _state = MutableLiveData<NoteDetailState>()
    val state = _state

    fun createOrUpdate(note: Note) {
        _state.value = NoteDetailState.Loading
        if (!note.id.isNullOrBlank()) {
            updateNote(note)
        } else {
            createNote(note)
        }
    }

    private fun createNote(note: Note) = viewModelScope.launch {
        repository.saveNote(note)
            .stateIn(scope = this)
            .onStart {
                _state.value = NoteDetailState.Loading
            }.catch { cause ->
                _state.value = NoteDetailState.Error(cause)
            }.collect {
                _state.value = NoteDetailState.UpdateOrSaveSuccess
            }
    }

    private fun updateNote(note: Note) = viewModelScope.launch {
        repository.updateNote(note)
            .stateIn(scope = this)
            .onStart {
                _state.value = NoteDetailState.Loading
            }.catch { cause ->
                _state.value = NoteDetailState.Error(cause)
            }.collect {
                _state.value = NoteDetailState.UpdateOrSaveSuccess
            }
    }

}