package ru.mavrinvladislav.testtask2024.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.mavrinvladislav.testtask2024.data.NoteMapper
import ru.mavrinvladislav.testtask2024.data.NoteRepositoryImpl
import ru.mavrinvladislav.testtask2024.data.NoteRoomLocalDataSource
import ru.mavrinvladislav.testtask2024.data.db.NotesDataBase
import ru.mavrinvladislav.testtask2024.domain.CreateNewNoteUseCase
import ru.mavrinvladislav.testtask2024.domain.EditNoteUseCase
import ru.mavrinvladislav.testtask2024.domain.GetNoteUseCase
import ru.mavrinvladislav.testtask2024.domain.SaveNoteUseCase

class NotesEditorViewModel(application: Application) : AndroidViewModel(application) {

    private val localDataSource =
        NoteRoomLocalDataSource(NotesDataBase.getInstance(application).notesDao())
    private val mapper = NoteMapper()
    private val repository = NoteRepositoryImpl(localDataSource, mapper)
    private val createNewNoteUseCase = CreateNewNoteUseCase(repository)
    private val editNoteUseCase = EditNoteUseCase(repository)
    private val saveNoteUseCase = SaveNoteUseCase(repository)
    private val getNoteUseCase = GetNoteUseCase(repository)

    val note = MutableStateFlow<NotesEditorState>(NotesEditorState.Initial)

    fun createNewNoteAndSaveToDb(isDraft: Boolean, inputTitle: String?, inputText: String?) {
        val title = parseString(inputTitle)
        val text = parseString(inputText)
        viewModelScope.launch {
            val newNote = createNewNoteUseCase(isDraft, title, text)
            saveNoteUseCase(newNote)
            note.value = NotesEditorState.Close
        }
    }

    suspend fun editNote(isDraft: Boolean, inputTitle: String?, inputText: String?) {
        val title = parseString(inputTitle)
        val text = parseString(inputText)
        note.value.let {
            it as NotesEditorState.Open
            val copy = it.note.copy(
                isDraft = isDraft,
                title = title,
                text = text
            )
            editNoteUseCase(copy)
            note.value = NotesEditorState.Close
        }
    }

    private fun parseString(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    fun getNote(noteId: Int) {
        viewModelScope.launch {
            note.value = NotesEditorState.Loading
            note.value = NotesEditorState.Open(getNoteUseCase(noteId))
        }
    }

    companion object {
        private const val LOG_TAG = "NotesEditorViewModel"
    }
}