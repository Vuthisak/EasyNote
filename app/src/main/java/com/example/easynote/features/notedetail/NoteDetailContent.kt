package com.example.easynote.features.notedetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.easynote.R
import com.example.easynote.entity.Note
import com.example.easynote.features.notedetail.state.NoteDetailState
import com.example.easynote.features.notedetail.state.NoteDetailUiState
import com.example.easynote.ui.theme.BackgroundLoading
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun NoteDetailContent(
    lifecycleScope: LifecycleCoroutineScope,
    viewModel: NoteDetailViewModel,
    note: Note,
    onCreateOrUpdate: (note: Note) -> Unit,
    onSuccess: () -> Unit
) {
    val uiState = remember { mutableStateOf(NoteDetailUiState()) }
    with(uiState.value) {
        val validateState = remember(titleState.value, descState.value) {
            titleState.value.trim().isNotBlank() && descState.value.trim().isNotBlank()
        }

        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                when (state) {
                    is NoteDetailState.Loading -> loadingState.value = true
                    is NoteDetailState.Finished -> loadingState.value = false
                    is NoteDetailState.UpdateOrSaveSuccess -> onSuccess()
                    is NoteDetailState.Error -> throw state.ex
                }
            }
        }
        Content(note, validateState, titleState, descState, loadingState) {
            onCreateOrUpdate(it)
        }
    }
}

@Composable
private fun Content(
    note: Note,
    validateState: Boolean,
    titleState: MutableState<String>,
    descState: MutableState<String>,
    loadingState: MutableState<Boolean>,
    onSaveOrUpdate: (note: Note) -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopBar(validateState) {
                    note.title = titleState.value
                    note.desc = descState.value
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
                TitleTextField(titleState)
                DescriptionTextField(descState)
            }
        }
        Loading(loadingState)
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
