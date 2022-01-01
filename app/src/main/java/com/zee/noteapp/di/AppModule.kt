package com.zee.noteapp.di

import android.app.Application
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.zee.noteDatabase.NoteDatabase
import com.zee.noteapp.domain.repository.NoteRepository
import com.zee.noteapp.domain.useCase.*
import com.zee.noteapp.featureNote.data.repository.NoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSqlDriver(app: Application): SqlDriver {
        return AndroidSqliteDriver(
            schema = NoteDatabase.Schema,
            context = app,
            name = "note.db"
        )
    }

    @Provides
    @Singleton
    fun provideNoteRepository(driver: SqlDriver): NoteRepository {
        return NoteRepositoryImpl(NoteDatabase(driver))
    }


    @Provides
    @Singleton
    fun provideNotesUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotesUseCase = GetNotesUseCase(repository),
            deleteNoteUseCase = DeleteNoteUseCase(repository),
            addNoteUseCase = AddNoteUseCase(repository),
            getNoteUseCase = GetNoteUseCase(repository)
        )
    }

}