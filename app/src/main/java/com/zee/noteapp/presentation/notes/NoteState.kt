package com.zee.noteapp.presentation.notes

import com.zee.noteapp.util.NoteOrder
import com.zee.noteapp.util.OrderType
import notedb.NoteEntity

data class NoteState(
    val notes:List<NoteEntity> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible :Boolean = false

)
