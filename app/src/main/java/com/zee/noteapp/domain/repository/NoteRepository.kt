package com.zee.noteapp.domain.repository

import kotlinx.coroutines.flow.Flow
import notedb.NoteEntity


interface NoteRepository {
    suspend fun getNodeById(id: Long): NoteEntity?
    fun getAllNotes(): Flow<List<NoteEntity>>
    suspend fun deleteNoteById(id: Long)
    suspend fun addNote(title: String, content: String, timeStamp: Long, color: Long, id: Long)

}