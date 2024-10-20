package ru.mavrinvladislav.testtask2024.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.mavrinvladislav.testtask2024.data.NoteLocalDataSource
import ru.mavrinvladislav.testtask2024.data.NoteRoomLocalDataSource
import ru.mavrinvladislav.testtask2024.data.db.NotesDao
import ru.mavrinvladislav.testtask2024.data.db.NotesDataBase

@Module
interface DataModule {
    @Binds
    fun bindRemoteDataSource(impl: NoteRoomLocalDataSource): NoteLocalDataSource

    companion object {
        @ApplicationScope
        @Provides
        fun provideNotesDao(
            application: Application
        ): NotesDao {
            return NotesDataBase.getInstance(application).notesDao()
        }
    }
}