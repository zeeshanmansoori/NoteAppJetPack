package com.zee.noteapp.presentation.notes

import com.zee.noteapp.util.NoteOrder
import notedb.NoteEntity

sealed class NoteEvent{
    data class Order(val noteOrder: NoteOrder):NoteEvent()
    data class DeleteNote(val note: NoteEntity):NoteEvent()
    object RestoreNote:NoteEvent()
    object ToggleOrderSection :NoteEvent()
}
