package com.example.easynote.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easynote.features.main.state.MainState
import com.example.easynote.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private var _state = MutableStateFlow<MainState>(MainState.Finished)
    val state = _state

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
                throw cause
//                _state.value = MainState.Error(cause)
            }.map { notes ->
                _state.value = MainState.OnGetListSuccess(notes, isReloaded)
            }.onCompletion {
                _state.value = MainState.Finished
            }.collect()
    }

}
