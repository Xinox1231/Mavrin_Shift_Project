package ru.mavrinvladislav.testtask2024.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.mavrinvladislav.testtask2024.data.NoteMapper
import ru.mavrinvladislav.testtask2024.data.NoteRepositoryImpl
import ru.mavrinvladislav.testtask2024.data.NoteRoomLocalDataSource
import ru.mavrinvladislav.testtask2024.data.db.NotesDataBase
import ru.mavrinvladislav.testtask2024.domain.CreateNewNoteUseCase
import ru.mavrinvladislav.testtask2024.domain.EditNoteUseCase
import ru.mavrinvladislav.testtask2024.domain.GetNoteUseCase
import ru.mavrinvladislav.testtask2024.domain.Note
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

    val note = MutableLiveData<Note>()

    val shouldCloseScreen = MutableLiveData<Boolean>(false)

    fun createNewNoteAndSaveToDb(inputTitle: String?, inputText: String?) {
        val title = parseString(inputTitle)
        val text = parseString(inputText)
        viewModelScope.launch {
            val note = createNewNoteUseCase(title, text)
            saveNoteUseCase(note)
            shouldCloseScreen.value = true
        }
    }

    suspend fun editNote(inputTitle: String?, inputText: String?) {
        val title = parseString(inputTitle)
        val text = parseString(inputText)
        note.value?.let {
            Log.d(LOG_TAG, it.id.toString())
            val copy = it.copy(
                title = title,
                text = text
            )
            editNoteUseCase(copy)
            shouldCloseScreen.value = true
        }
    }

    private fun parseString(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    fun getNote(noteId: Int) {
        viewModelScope.launch {
            note.value = getNoteUseCase(noteId)
        }
    }

    companion object {
        private const val LOG_TAG = "NotesEditorViewModel"
    }
}