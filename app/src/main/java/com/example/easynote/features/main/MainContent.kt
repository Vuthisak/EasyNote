package com.example.easynote.features.main

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
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
import androidx.compose.material.icons.outlined.PowerSettingsNew
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.easynote.R
import com.example.easynote.base.BaseContent
import com.example.easynote.entity.Note
import com.example.easynote.features.login.LoginActivity
import com.example.easynote.features.main.state.MainState
import com.example.easynote.features.main.state.MainUiState
import com.example.easynote.features.notedetail.NoteDetailActivity
import com.example.easynote.features.notedetail.NoteDetailActivity.Companion.EXTRA_NOTE
import com.example.easynote.util.Loading
import com.example.easynote.util.formattedDate
import com.example.easynote.util.getOrDefault
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainContent(
    private val activity: MainActivity,
    private val viewModel: MainViewModel
) : BaseContent() {

    @Composable
    override fun register() {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
        ) {
            getNotesIfResultOk(it)
        }
        Scaffold(
            floatingActionButton = {
                FloatingButton {
                    val intent = Intent(activity, NoteDetailActivity::class.java)
                    launcher.launch(intent)
                }
            },
            topBar = { TopBar() },
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Red)
        ) {
            HandleState(modifier = Modifier.padding(it)) { note ->
                val intent = Intent(activity, NoteDetailActivity::class.java)
                intent.putExtra(EXTRA_NOTE, note)
                launcher.launch(intent)
            }
        }
    }

    private fun getNotesIfResultOk(it: ActivityResult) {
        if (it.resultCode == Activity.RESULT_OK) {
            viewModel.getNotes(true)
        }
    }

    @Composable
    private fun HandleState(modifier: Modifier, onItemClick: (note: Note) -> Unit) {
        val uiState = remember { MainUiState() }
        activity.lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                when (state) {
                    is MainState.Finished -> uiState.hideLoading()
                    is MainState.Loading -> showLoading(uiState)
                    is MainState.OnGetListSuccess -> onSuccess(uiState, state)
                    is MainState.Error -> onError(state.ex)
                }
            }
        }
        Box(modifier = modifier.fillMaxSize()) {
            Loading(uiState.loadingState)
            AnimatedVisibility(
                visible = uiState.visibleState.value,
                enter = EnterTransition.None,
                exit = ExitTransition.None
            ) {
                SwipeRefresh(
                    state = uiState.isRefreshingState,
                    onRefresh = {
                        uiState.visibleState.value = false
                        uiState.items.clear()
                        viewModel.getNotes(true)
                    }) {
                    BodyContent(uiState.items, onItemClick = onItemClick, onDelete = {
                        viewModel.removeNote(it.id.getOrDefault())
                    })
                }
            }
        }
    }

    private fun showLoading(uiState: MainUiState) {
        uiState.visibleState.value = false
        uiState.loadingState.value = !uiState.isRefreshingState.isRefreshing
    }

    private fun onSuccess(
        uiState: MainUiState,
        state: MainState.OnGetListSuccess
    ) {
        uiState.isRefreshingState.isRefreshing = false
        uiState.visibleState.value = true
        if (state.isReloaded) uiState.items.clear()
        uiState.items.addAll(state.items)
    }

    private fun onError(ex: Throwable?) {
        Toast.makeText(activity, ex?.message, Toast.LENGTH_SHORT).show()
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

    @OptIn(ExperimentalMaterialApi::class)
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

    @OptIn(ExperimentalMaterialApi::class)
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
        val shouldShowDialog = remember { mutableStateOf(false) }
        if (shouldShowDialog.value) {
            showAlertDialog {
                shouldShowDialog.value = false
            }
        }
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
                IconButton(onClick = { shouldShowDialog.value = true }) {
                    Icon(
                        imageVector = Icons.Outlined.PowerSettingsNew,
                        contentDescription = "Save Or Update"
                    )
                }
            }
        )
    }

    @Composable
    private fun showAlertDialog(onCancel: () -> Unit) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = {
                    FirebaseAuth.getInstance().signOut()
                    gotoLoginScreen()
                })
                { Text(text = stringResource(id = R.string.action_yes)) }
            },
            dismissButton = {
                TextButton(onClick = onCancel)
                { Text(text = stringResource(id = R.string.action_no)) }
            },
            title = { Text(text = stringResource(id = R.string.text_logout)) },
            text = { Text(text = stringResource(id = R.string.text_logout_desc)) }
        )
    }

    private fun gotoLoginScreen() {
        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
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
                        color = Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = 0.5.dp,
                        color = Color.Black,
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
                    style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    text = note.desc.getOrDefault(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(fontSize = 14.sp)
                )
            }
        }
    }

}
