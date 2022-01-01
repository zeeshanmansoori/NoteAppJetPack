package com.zee.noteapp.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zee.noteapp.domain.useCase.NoteUseCases
import com.zee.noteapp.util.NoteOrder
import com.zee.noteapp.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import notedb.NoteEntity
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val notesUseCase: NoteUseCases) : ViewModel() {

    private val _state = mutableStateOf(NoteState())
    val state: State<NoteState> = _state
    private var recentlyDeletedNote: NoteEntity? = null
    private var getNotesJob: Job? = null


    init {
        getNotesByOrder(NoteOrder.Date(orderType = OrderType.Descending))
    }

    fun onEvent(event: NoteEvent) {


        when (event) {
            is NoteEvent.Order -> {

                if (state.value.noteOrder::class == event.noteOrder::class && state.value.noteOrder.orderType == event.noteOrder.orderType)
                    return

                getNotesByOrder(event.noteOrder)
            }

            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    notesUseCase.deleteNoteUseCase.invoke(event.note)
                    recentlyDeletedNote = event.note
                }
            }

            is NoteEvent.RestoreNote -> {
                viewModelScope.launch {
                    recentlyDeletedNote?.let { note ->
                        notesUseCase.addNoteUseCase(note = note)
                        recentlyDeletedNote = null
                    }

                }
            }

            is NoteEvent.ToggleOrderSection -> {
                _state.value =
                    _state.value.copy(isOrderSectionVisible = !_state.value.isOrderSectionVisible)
            }
        }
    }

    private fun getNotesByOrder(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = notesUseCase.getNotesUseCase(noteOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }.launchIn(viewModelScope)
    }
}