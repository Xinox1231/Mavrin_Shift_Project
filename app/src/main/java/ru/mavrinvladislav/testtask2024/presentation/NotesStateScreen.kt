package ru.mavrinvladislav.testtask2024.presentation

import ru.mavrinvladislav.testtask2024.domain.Note


sealed class NotesStateScreen {
    data object NoContent : NotesStateScreen()
    class ContentLoaded(
        val list: List<Note>
    ) : NotesStateScreen()
}