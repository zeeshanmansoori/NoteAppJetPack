package com.zee.noteapp.featureNote.data.repository

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.zee.noteDatabase.NoteDatabase
import com.zee.noteapp.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import notedb.NoteEntity

class NoteRepositoryImpl(db: NoteDatabase) : NoteRepository {


    private val queries = db.noteEntityQueries

    override suspend fun getNodeById(id: Long): NoteEntity? {
        return withContext(Dispatchers.IO) {
            queries.getNoteById(id).executeAsOneOrNull()
        }
    }

    override fun getAllNotes(): Flow<List<NoteEntity>> {
        return queries.getAllNotes().asFlow().mapToList()
    }

    override suspend fun deleteNoteById(id: Long) {
        queries.deleteNoteById(id)
    }

    override suspend fun addNote(
        title: String,
        content: String,
        timeStamp: Long,
        color: Long,
        id: Long
    ) {
        queries.insertNote(id, title, content, timeStamp, color)
    }
}