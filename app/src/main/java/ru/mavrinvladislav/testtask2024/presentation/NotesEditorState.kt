package ru.mavrinvladislav.testtask2024.presentation

import ru.mavrinvladislav.testtask2024.domain.Note

sealed class NotesEditorState() {

    data object Initial : NotesEditorState()
    data object Loading : NotesEditorState()
    class Open(val note: Note) : NotesEditorState()
    data object Close : NotesEditorState()
}

