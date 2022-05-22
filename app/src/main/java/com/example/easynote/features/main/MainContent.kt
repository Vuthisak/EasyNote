package com.example.easynote.features.main

import android.app.Activity
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
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.easynote.features.notedetail.NoteDetailActivity.Companion.EXTRA_NOTE
import com.example.easynote.util.formattedDate
import com.example.easynote.util.getOrDefault

@Composable
fun MainContent(
    state: MainState,
    intent: Intent,
    onDelete: (note: Note) -> Unit,
    onResultOk: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            onResultOk()
        }
    }
    Scaffold(
        floatingActionButton = { FloatingButton { launcher.launch(intent) } },
        topBar = { TopBar() },
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red)
    ) {
        HandleState(state, onDelete) { note ->
            intent.putExtra(EXTRA_NOTE, note)
            launcher.launch(intent)
        }
    }
}

@Composable
private fun HandleState(
    state: MainState,
    onDelete: (note: Note) -> Unit,
    onItemClick: (note: Note) -> Unit
) {
    val items = remember { mutableStateListOf<Note>() }
    val visibleState = remember { mutableStateOf(false) }
    when (state) {
        is MainState.Loading -> {
            visibleState.value = false
            Loading()
        }
        is MainState.OnGetListSuccess -> {
            visibleState.value = true
            if (state.isReloaded) {
                items.clear()
            }
            items.addAll(state.items)
        }
        is MainState.Error -> {
            Toast.makeText(LocalContext.current, state.ex.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    AnimatedVisibility(
        visible = visibleState.value,
        exit = ExitTransition.None,
        enter = EnterTransition.None
    ) {
        BodyContent(items, onDelete, onItemClick)
    }
}

@Composable
private fun BodyContent(
    noteItems: SnapshotStateList<Note>,
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
        Text(text = "No Data", fontWeight = FontWeight.SemiBold, fontSize = 24.sp)
    }
}

@Composable
private fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun NoteList(
    noteItems: SnapshotStateList<Note>,
    onDelete: (note: Note) -> Unit,
    onItemClick: (note: Note) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 8.dp),
            text = stringResource(id = R.string.title_list_note),
            style = TextStyle(fontWeight = FontWeight.SemiBold)
        )
        LazyColumn(
            modifier =
            Modifier.fillMaxWidth()
        ) {
            itemsIndexed(items = noteItems) { index, item ->
                val dismissState = rememberDismissState(
                    confirmStateChange = { it != DismissValue.DismissedToEnd }
                )
                var itemAppeared by remember { mutableStateOf(true) }
                val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
                if (dismissState.targetValue == DismissValue.DismissedToEnd) {
                    onDelete(item)
                    noteItems.removeAt(index)
                    itemAppeared = false
                }
                AnimatedVisibility(
                    visible = itemAppeared && !isDismissed,
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
