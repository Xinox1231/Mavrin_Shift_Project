package ru.mavrinvladislav.testtask2024.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.mavrinvladislav.testtask2024.domain.CreateNewNoteUseCase
import ru.mavrinvladislav.testtask2024.domain.DeleteNoteUseCase
import ru.mavrinvladislav.testtask2024.domain.EditNoteUseCase
import ru.mavrinvladislav.testtask2024.domain.GetNoteUseCase
import ru.mavrinvladislav.testtask2024.domain.Note
import ru.mavrinvladislav.testtask2024.domain.SaveNoteUseCase
import javax.inject.Inject

class NoteEditorViewModel @Inject constructor(
    private val createNewNoteUseCase: CreateNewNoteUseCase,
    private val editNoteUseCase: EditNoteUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val getNoteUseCase: GetNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val _note = MutableStateFlow<NotesEditorState>(NotesEditorState.Initial)
    val note: SharedFlow<NotesEditorState>
        get() = _note.asSharedFlow()

    var shouldSkipSaveOnPause = false

    fun createNewNoteAndSaveToDb(isDraft: Boolean, inputTitle: String?, inputText: String?) {
        val title = parseString(inputTitle)
        val text = parseString(inputText)
        viewModelScope.launch {
            val newNote = createNewNoteUseCase(isDraft, title, text)
            saveNoteUseCase(newNote)
            shouldSkipSaveOnPause = true
            _note.value = NotesEditorState.Close
        }
    }

    fun editNote(isDraft: Boolean, inputTitle: String?, inputText: String?) {
        viewModelScope.launch {
            val title = parseString(inputTitle)
            val text = parseString(inputText)

            when (val currentState = _note.value) {
                is NotesEditorState.Open -> {
                    val copy = currentState.note.copy(
                        isDraft = isDraft,
                        title = title,
                        text = text
                    )
                    editNoteUseCase(copy)
                    shouldSkipSaveOnPause = true
                    _note.value = NotesEditorState.Close
                }

                else -> {
                }
            }
        }
    }


    fun deleteNote(noteId: Int) {
        viewModelScope.launch {
            deleteNoteUseCase(noteId)
            shouldSkipSaveOnPause = true
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