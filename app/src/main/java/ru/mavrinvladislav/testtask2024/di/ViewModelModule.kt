package ru.mavrinvladislav.testtask2024.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.mavrinvladislav.testtask2024.presentation.NoteEditorViewModel
import ru.mavrinvladislav.testtask2024.presentation.NotesViewModel

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(NotesViewModel::class)
    @Binds
    fun bindNotesViewModel(impl: NotesViewModel): ViewModel

    @IntoMap
    @ViewModelKey(NoteEditorViewModel::class)
    @Binds
    fun bindNoteEditorViewModel(impl: NoteEditorViewModel): ViewModel
}