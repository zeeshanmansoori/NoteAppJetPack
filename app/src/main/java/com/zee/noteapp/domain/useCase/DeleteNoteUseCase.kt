package com.zee.noteapp.domain.useCase

import com.zee.noteapp.domain.repository.NoteRepository
import notedb.NoteEntity

class DeleteNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: NoteEntity) {
        repository.deleteNoteById(note.id)
    }
}