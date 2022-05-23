package com.example.easynote.features.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easynote.R
import com.example.easynote.entity.Note
import com.example.easynote.features.notedetail.NoteDetailActivity
import com.example.easynote.features.notedetail.NoteDetailActivity.Companion.EXTRA_NOTE
import com.example.easynote.util.formattedDate
import com.example.easynote.util.getOrDefault
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay

@Composable
fun MainContent(
    context: Context,
    viewModel: MainViewModel
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            viewModel.getNotes(true)
        }
    }
    Scaffold(
        floatingActionButton = {
            FloatingButton {
                launcher.launch(Intent(context, NoteDetailActivity::class.java))
            }
        },
        topBar = { TopBar() },
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red)
    ) {
        HandleState(
            modifier = Modifier.padding(it),
            viewModel = viewModel,
            onItemClick = { note ->
                val intent = Intent(context, NoteDetailActivity::class.java)
                intent.putExtra(EXTRA_NOTE, note)
                launcher.launch(intent)
            })
    }
}

@Composable
private fun HandleState(
    modifier: Modifier,
    viewModel: MainViewModel,
    onItemClick: (note: Note) -> Unit,
) {
    val items = remember { mutableStateListOf<Note>() }
    val isRefreshingState = rememberSwipeRefreshState(false)
    val loadingState = remember { mutableStateOf(true) }
    val visibleState = remember { mutableStateOf(false) }
    when (val state = viewModel.state.collectAsState().value) {
        is MainState.Loading -> {
            visibleState.value = false
            loadingState.value = !isRefreshingState.isRefreshing
        }
        is MainState.OnGetListSuccess -> {
            loadingState.value = false
            isRefreshingState.isRefreshing = false
            visibleState.value = true
            if (state.isReloaded) {
                items.clear()
            }
            items.addAll(state.items)
            viewModel.finished()
        }
        is MainState.Error -> {
            Toast.makeText(LocalContext.current, state.ex.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    Box(modifier = modifier.fillMaxSize()) {
        Loading(loadingState.value)
        AnimatedVisibility(
            visible = visibleState.value,
            enter = EnterTransition.None,
            exit = ExitTransition.None
        ) {
            SwipeRefresh(
                state = isRefreshingState,
                onRefresh = {
                    visibleState.value = false
                    items.clear()
                    viewModel.getNotes(true)
                }) {
                BodyContent(items, onItemClick = onItemClick, onDelete = {
                    viewModel.removeNote(it.id.getOrDefault())
                })
            }
        }
    }
}

@Composable
private fun BodyContent(
    noteItems: MutableList<Note>,
    onDelete: (note: Note) -> Unit,
    onItemClick: (note: Note) -> Unit
) {
    if (noteItems.isEmpty()) {
        Empty()
    } else {
        NoteList(noteItems = noteItems, onDelete, onItemClick)
    }
}

@Composable
private fun Empty() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.no_data),
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp
        )
    }
}

@Composable
private fun Loading(loadingState: Boolean) {
    AnimatedVisibility(
        visible = loadingState,
        enter = EnterTransition.None,
        exit = ExitTransition.None
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun NoteList(
    noteItems: MutableList<Note>,
    onDelete: (note: Note) -> Unit,
    onItemClick: (note: Note) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 8.dp),
            text = stringResource(id = R.string.title_list_note),
            style = TextStyle(fontWeight = FontWeight.SemiBold)
        )
        LazyColumn(
            modifier =
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            items(items = noteItems, { it.id.getOrDefault() }) { item ->
                val dismissState = rememberDismissState(DismissValue.Default)
                val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
                if (dismissState.isDismissed(DismissDirection.EndToStart)
                    && dismissState.dismissDirection == DismissDirection.EndToStart
                ) {
                    LaunchedEffect(dismissState) {
                        delay(300)
                        onDelete(item)
                        noteItems.remove(item)
                    }
                }
                AnimatedVisibility(
                    visible = !isDismissed
                ) {
                    SwipeToDismiss(
                        state = dismissState,
                        modifier = Modifier.padding(vertical = 1.dp),
                        directions = setOf(DismissDirection.EndToStart),
                        dismissThresholds = { direction ->
                            FractionalThreshold(
                                if (direction == DismissDirection.EndToStart) 0.5f else 0f
                            )
                        },
                        background = { SwipeToDismissBackground(dismissState) },
                        dismissContent = { MainRow(item, onItemClick) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SwipeToDismissBackground(dismissState: DismissState) {
    val scale by animateFloatAsState(
        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
    )
    Box(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            Icons.Default.Delete,
            tint = Color.Red,
            contentDescription = "Action",
            modifier = Modifier.scale(scale)
        )
    }
}

@Composable
private fun FloatingButton(onFloatingActionClick: () -> Unit) {
    FloatingActionButton(onClick = onFloatingActionClick) {
        Image(imageVector = Icons.Filled.Add, contentDescription = "Add")
    }
}

@Composable
private fun TopBar() {
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
    )
}

@Composable
private fun MainRow(note: Note, onItemClick: (note: Note) -> Unit) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = Color(note.backgroundColor),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable(onClick = {
                    onItemClick(note)
                })
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = note.updatedAt.formattedDate(),
                style = TextStyle(
                    textAlign = TextAlign.End,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light
                )
            )
            Text(
                text = note.title.getOrDefault(),
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(fontWeight = FontWeight.SemiBold)
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 16.dp),
                text = note.desc.getOrDefault(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
