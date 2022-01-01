package com.zee.noteapp.presentation.addEditNote

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zee.noteapp.presentation.addEditNote.components.TransparentTextField
import com.zee.noteapp.util.noteColors
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val noteBackgroundColor = remember {
        Animatable(
            Color(if (noteColor != -1) noteColor else viewModel.noteColor.value)
        )
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditNoteViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.message)
                }
                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }

            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(AddEditNoteEvent.SaveNote) },

            ) {

                Icon(imageVector = Icons.Default.Save, contentDescription = "Save note")
            }


        },
        scaffoldState = scaffoldState
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundColor.value)
                .padding(16.dp)
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                noteColors.forEach { color ->
                    val colorInt = color.toArgb()

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(15.dp, CircleShape)
                            .clip(CircleShape)
                            .border(
                                width = 3.dp,
                                color = if (viewModel.noteColor.value == colorInt) Color.Black else Color.Transparent,
                                shape = CircleShape
                            ).background(color = Color(colorInt))
                            .clickable {
                                scope.launch {
                                    noteBackgroundColor.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(580)
                                    )

                                    viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                                }
                            }
                    )

                }

            }
            Spacer(modifier = Modifier.height(16.dp))
            TransparentTextField(
                text = titleState.text,
                hint = titleState.hint,
                singleLine = true,
                isHintVisible = titleState.isHintVisible,
                onValueChange = { viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it)) },
                onFocusChange = { viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it)) },
                textStyle = MaterialTheme.typography.h5
            )

            TransparentTextField(
                text = contentState.text,
                hint = contentState.hint,
                singleLine = false,
                isHintVisible = contentState.isHintVisible,
                onValueChange = { viewModel.onEvent(AddEditNoteEvent.EnteredContent(it)) },
                onFocusChange = { viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it)) },
                textStyle = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxSize()
            )
        }

    }

}