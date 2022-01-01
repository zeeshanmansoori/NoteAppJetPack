package com.zee.noteapp.domain.useCase

import com.zee.noteapp.domain.repository.NoteRepository
import com.zee.noteapp.util.InvalidNoteException
import notedb.NoteEntity

class AddNoteUseCase(private val repository: NoteRepository) {

    @Throws()
    suspend operator fun invoke(note: NoteEntity) {
        if (note.title.isBlank())
            throw  InvalidNoteException("The title of the note can't be empty.")
        if (note.content.isBlank())
            throw  InvalidNoteException("The content of the note can't be empty.")

        repository.addNote(
            note.title, note.content, timeStamp = note.timeStamp, color = note.color, id = note.id
        )

    }
}