package com.zee.noteapp.presentation.addEditNote

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zee.noteapp.domain.useCase.NoteUseCases
import com.zee.noteapp.util.noteColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import notedb.NoteEntity
import javax.inject.Inject


@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCase: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val _noteTitle = mutableStateOf(NoteTextFieldState(hint = "Enter title"))
    val noteTitle: State<NoteTextFieldState> = _noteTitle


    private val _noteContent = mutableStateOf(NoteTextFieldState(hint = "Enter some content"))
    val noteContent: State<NoteTextFieldState> = _noteContent


    private val _noteColor = mutableStateOf(noteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentId: Long? = null

    init {

        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCase.getNoteUseCase(noteId.toLong())?.also { note ->
                        currentId = note.id


                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )

                        _noteContent.value = noteContent.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )

                        _noteColor.value = note.color.toInt()


                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }


    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(text = event.value)
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value =
                    noteTitle.value.copy(isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank())

            }

            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(text = event.value)
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value =
                    noteContent.value.copy(isHintVisible = !event.focusState.isFocused && noteContent.value.text.isBlank())

            }

            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color
            }


            //TODO("update id ")
            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    kotlin.runCatching {
                        noteUseCase.addNoteUseCase(
                            NoteEntity(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timeStamp = System.currentTimeMillis(),
                                color = noteColor.value.toLong(),
                                id = currentId ?: 0L

                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    }.onFailure {
                        _eventFlow.emit(UiEvent.ShowSnackBar(it.localizedMessage ?: ""))
                    }
                }
            }


        }
    }
}