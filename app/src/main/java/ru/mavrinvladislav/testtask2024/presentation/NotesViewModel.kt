package ru.mavrinvladislav.testtask2024.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.mavrinvladislav.testtask2024.domain.DeleteNoteUseCase
import ru.mavrinvladislav.testtask2024.domain.EditNoteUseCase
import ru.mavrinvladislav.testtask2024.domain.GetAllNotesUseCase
import ru.mavrinvladislav.testtask2024.domain.Note
import ru.mavrinvladislav.testtask2024.domain.SearchNotesUseCase
import javax.inject.Inject

class NotesViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val searchNotesUseCase: SearchNotesUseCase,
    private val editNoteUseCase: EditNoteUseCase
) : ViewModel() {

    private val searchState = MutableSharedFlow<NotesStateScreen>()

    val state = getAllNotesUseCase().map {
        if (it.isEmpty()) {
            NotesStateScreen.NoContent
        } else {
            NotesStateScreen.ContentLoaded(it) as NotesStateScreen
        }
    }.onStart {
        emit(NotesStateScreen.Loading)
    }.mergeWith(searchState)

    private fun <T> Flow<T>.mergeWith(another: Flow<T>): Flow<T> {
        return merge(this, another)
    }

    fun changePinState(note: Note) {
        viewModelScope.launch {
            val newNote = note.copy(isPinned = !note.isPinned)
            editNoteUseCase(newNote)
        }
    }

    fun searchNote(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) {
                val dbFlow = getAllNotesUseCase().map {
                    if (it.isEmpty()) {
                        NotesStateScreen.NoContent
                    } else {
                        NotesStateScreen.ContentLoaded(it) as NotesStateScreen
                    }
                }
                searchState.emitAll(dbFlow)
            } else {
                val dbFlow = searchNotesUseCase(query).map {
                    if (it.isEmpty()) {
                        NotesStateScreen.NoContent
                    } else {
                        NotesStateScreen.ContentLoaded(it) as NotesStateScreen
                    }
                }
                searchState.emitAll(dbFlow)
            }
        }
    }


}