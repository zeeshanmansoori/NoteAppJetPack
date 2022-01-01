package com.zee.noteapp.domain.useCase

import com.zee.noteapp.domain.repository.NoteRepository
import notedb.NoteEntity

class GetNoteUseCase(private val repository: NoteRepository) {


    suspend operator fun invoke(id:Long): NoteEntity? {
        return repository.getNodeById(id)

    }
}