package com.zee.noteapp.presentation.notes

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.zee.noteapp.presentation.notes.components.NoteItem
import com.zee.noteapp.presentation.notes.components.OrderSection
import com.zee.noteapp.util.Screen
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun NoteScreen(
    navController: NavController,
    viewModel: NoteViewModel = hiltViewModel()
) {


    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(scaffoldState = scaffoldState,
        floatingActionButton = {


            FloatingActionButton(onClick = {
                navController.navigate(Screen.AddEditNoteScreen.route)
            }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
            }


        }) {


        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {


                Text(
                    text = "Your Note",
                    style = MaterialTheme.typography.h6
                )

                IconButton(onClick = { viewModel.onEvent(NoteEvent.ToggleOrderSection) }) {
                    Icon(imageVector = Icons.Default.Sort, contentDescription = null)
                }

            }

                AnimatedVisibility(
                    visible = state.isOrderSectionVisible,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {

                    OrderSection(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                        noteOrder = state.noteOrder,
                        onOrderChange = { viewModel.onEvent(NoteEvent.Order(it)) }
                    )

                }

                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.notes) { note ->
                        NoteItem(note = note, modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth()

                            .clickable {

                                navController.navigate(
                                    Screen.AddEditNoteScreen.route +
                                            "?noteId=${note.id.toInt()}&noteColor=${note.color}"
                                )

                            },
                            onDeleteClick = {
                                viewModel.onEvent(NoteEvent.DeleteNote(note = note))
                                scope.launch {
                                    val result = scaffoldState.snackbarHostState.showSnackbar(
                                        "Note Deleted",
                                        actionLabel = "Undo"
                                    )

                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.onEvent(NoteEvent.RestoreNote)
                                    }

                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

        }
    }
}

@ExperimentalAnimationApi
@androidx.compose.ui.tooling.preview.Preview
@androidx.compose.runtime.Composable
fun NoteScreenPreview() {
    NoteScreen(navController = rememberNavController())
}