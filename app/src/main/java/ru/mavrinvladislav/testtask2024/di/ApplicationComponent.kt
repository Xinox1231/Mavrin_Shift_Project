package ru.mavrinvladislav.testtask2024.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.mavrinvladislav.testtask2024.presentation.NotesEditorState
import ru.mavrinvladislav.testtask2024.ui.NoteEditorFragment
import ru.mavrinvladislav.testtask2024.ui.NotesFragment

@Component(modules = [DomainModule::class, DataModule::class, ViewModelModule::class])
@ApplicationScope
interface ApplicationComponent {

    fun inject(fragment: NotesFragment)
    fun inject(fragment: NoteEditorFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}