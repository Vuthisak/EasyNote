package com.example.easynote.features.notedetail

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.easynote.R
import com.example.easynote.base.BaseContent
import com.example.easynote.entity.Note
import com.example.easynote.features.notedetail.state.NoteDetailState
import com.example.easynote.features.notedetail.state.NoteDetailUiState
import com.example.easynote.ui.theme.BackgroundLoading
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NoteDetailContent(
    private val activity: NoteDetailActivity,
    private val viewModel: NoteDetailViewModel,
    private val note: Note
) : BaseContent() {

    @Composable
    override fun register() {
        val uiState = remember { NoteDetailUiState(note) }
        with(uiState) {
            val validateState = remember(titleState.value, descState.value) {
                isFormValid(uiState)
            }
            handleState(this)
            Content(this, note, validateState) { viewModel.createOrUpdate(it) }
        }
    }

    private fun isFormValid(uiState: NoteDetailUiState) =
        uiState.run {
            titleState.value.trim().isNotBlank() && descState.value.trim().isNotBlank()
        }

    @Composable
    private fun handleState(uiState: NoteDetailUiState) {
        activity.lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                when (state) {
                    is NoteDetailState.Loading -> uiState.showLoading()
                    is NoteDetailState.Finished -> uiState.hideLoading()
                    is NoteDetailState.UpdateOrSaveSuccess -> onUpdateOrSaveSuccess()
                    is NoteDetailState.Error -> onError(state.ex)
                }
            }
        }
    }

    private fun onUpdateOrSaveSuccess() {
        activity.setResult(RESULT_OK)
        activity.finish()
    }

    private fun onError(ex: Throwable) {
        Toast.makeText(activity, ex.message, Toast.LENGTH_SHORT).show()
    }

    @Composable
    private fun Content(
        uiState: NoteDetailUiState,
        note: Note,
        validateState: Boolean,
        onSaveOrUpdate: (note: Note) -> Unit
    ) {
        Box(Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
                    TopBar(validateState) {
                        note.title = uiState.titleState.value
                        note.desc = uiState.descState.value
                        onSaveOrUpdate(note)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Red)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    TitleTextField(uiState.titleState)
                    DescriptionTextField(uiState.descState)
                }
            }
            Loading(uiState.loadingState)
        }
    }

    @Composable
    private fun TitleTextField(titleState: MutableState<String>) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = titleState.value,
            onValueChange = { titleState.value = it },
            label = { Text(text = stringResource(id = R.string.text_title)) },
            maxLines = 1,
            colors = defaultTextFieldColors(),
        )
    }

    @Composable
    private fun DescriptionTextField(descState: MutableState<String>) {
        TextField(
            modifier = Modifier.fillMaxSize(),
            value = descState.value,
            onValueChange = { descState.value = it },
            label = { Text(text = stringResource(id = R.string.text_desc)) },
            maxLines = 50,
            colors = defaultTextFieldColors(),
        )
    }

    @Composable
    private fun defaultTextFieldColors() = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
    )

    @Composable
    private fun Loading(loadingState: MutableState<Boolean>) {
        AnimatedVisibility(
            enter = EnterTransition.None,
            exit = ExitTransition.None,
            visible = loadingState.value,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundLoading)
                    .clickable { /* No Implementation */ }
                    .focusable(true)
            ) { CircularProgressIndicator() }
        }
    }

    @Composable
    private fun TopBar(validateState: Boolean, onSaveOrUpdate: () -> Unit) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            elevation = 0.dp,
            backgroundColor = Color.Transparent,
            actions = {
                if (validateState) {
                    IconButton(onClick = { onSaveOrUpdate() }) {
                        Icon(
                            imageVector = Icons.Outlined.Archive,
                            contentDescription = "Save Or Update"
                        )
                    }
                }
            }
        )
    }

}
