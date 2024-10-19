package ru.mavrinvladislav.testtask2024.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.mavrinvladislav.testtask2024.data.NoteMapper
import ru.mavrinvladislav.testtask2024.data.NoteRepositoryImpl
import ru.mavrinvladislav.testtask2024.data.NoteRoomLocalDataSource
import ru.mavrinvladislav.testtask2024.data.db.NotesDataBase
import ru.mavrinvladislav.testtask2024.domain.DeleteNoteUseCase
import ru.mavrinvladislav.testtask2024.domain.GetAllNotesUseCase
import ru.mavrinvladislav.testtask2024.domain.Note

class NotesViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val localDataSource =
        NoteRoomLocalDataSource(NotesDataBase.getInstance(application).notesDao())
    private val mapper = NoteMapper()
    private val repository = NoteRepositoryImpl(localDataSource, mapper)
    private val getAllNotesUseCase = GetAllNotesUseCase(repository)
    private val deleteNoteUseCase = DeleteNoteUseCase(repository)

    val state = getAllNotesUseCase().map {
        if (it.isEmpty()) {
            NotesStateScreen.NoContent
        } else {
            NotesStateScreen.ContentLoaded(it) as NotesStateScreen
        }
    }.onStart {
        emit(NotesStateScreen.Loading)
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            deleteNoteUseCase(note)
        }
    }
}