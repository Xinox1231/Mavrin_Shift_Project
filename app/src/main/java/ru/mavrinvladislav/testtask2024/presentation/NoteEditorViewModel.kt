package ru.mavrinvladislav.testtask2024.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.mavrinvladislav.testtask2024.domain.CreateNewNoteUseCase
import ru.mavrinvladislav.testtask2024.domain.EditNoteUseCase
import ru.mavrinvladislav.testtask2024.domain.GetNoteUseCase
import ru.mavrinvladislav.testtask2024.domain.SaveNoteUseCase
import javax.inject.Inject

class NoteEditorViewModel @Inject constructor(
    private val createNewNoteUseCase: CreateNewNoteUseCase,
    private val editNoteUseCase: EditNoteUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val getNoteUseCase: GetNoteUseCase
) : ViewModel() {

    private val _note = MutableStateFlow<NotesEditorState>(NotesEditorState.Initial)
    val note: SharedFlow<NotesEditorState>
        get() = _note.asSharedFlow()

    fun createNewNoteAndSaveToDb(isDraft: Boolean, inputTitle: String?, inputText: String?) {
        val title = parseString(inputTitle)
        val text = parseString(inputText)
        viewModelScope.launch {
            val newNote = createNewNoteUseCase(isDraft, title, text)
            saveNoteUseCase(newNote)
            _note.value = NotesEditorState.Close
        }
    }

    suspend fun editNote(isDraft: Boolean, inputTitle: String?, inputText: String?) {
        val title = parseString(inputTitle)
        val text = parseString(inputText)
        _note.value.let {
            it as NotesEditorState.Open
            val copy = it.note.copy(
                isDraft = isDraft,
                title = title,
                text = text
            )
            editNoteUseCase(copy)
            _note.value = NotesEditorState.Close
        }
    }

    private fun parseString(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    fun getNote(noteId: Int) {
        viewModelScope.launch {
            _note.value = NotesEditorState.Loading
            _note.value = NotesEditorState.Open(getNoteUseCase(noteId))
        }
    }

    companion object {
        private const val LOG_TAG = "NotesEditorViewModel"
    }
}