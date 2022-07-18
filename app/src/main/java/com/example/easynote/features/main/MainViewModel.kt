package com.example.easynote.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easynote.data.LocalPreferences
import com.example.easynote.features.main.state.MainState
import com.example.easynote.repository.NoteRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val noteRepository: NoteRepository,
    private val preferences: LocalPreferences
) : ViewModel() {

    private var _state = MutableStateFlow<MainState>(MainState.Finished)
    val state = _state

    fun logout() {
        preferences.removePasscode()
    }

    fun removeNote(noteId: String) = viewModelScope.launch {
        noteRepository.removeNote(noteId)
            .catch { cause ->
                _state.value = MainState.Error(cause)
            }.stateIn(scope = this)
    }

    fun getNotes(isReloaded: Boolean = false) = viewModelScope.launch {
        noteRepository.getNotes()
            .onStart {
                _state.value = MainState.Loading
            }.catch { cause ->
                _state.value = MainState.Error(cause)
            }.map { notes ->
                _state.value = MainState.OnGetListSuccess(notes, isReloaded)
            }.onCompletion {
                _state.value = MainState.Finished
            }.collect()
    }

}
