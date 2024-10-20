package ru.mavrinvladislav.testtask2024.presentation

import ru.mavrinvladislav.testtask2024.domain.Note


sealed class NotesStateScreen {
    data object NoContent : NotesStateScreen()
    data object Loading : NotesStateScreen()
    class ContentLoaded(
        val list: List<Note>
    ) : NotesStateScreen()
}