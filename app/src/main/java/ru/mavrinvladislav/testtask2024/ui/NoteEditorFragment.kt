package ru.mavrinvladislav.testtask2024.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import ru.mavrinvladislav.testtask2024.databinding.FragmentNoteEditorBinding
import ru.mavrinvladislav.testtask2024.domain.Note
import ru.mavrinvladislav.testtask2024.presentation.NotesEditorState
import ru.mavrinvladislav.testtask2024.presentation.NotesEditorViewModel

class NoteEditorFragment : Fragment() {

    private var _binding: FragmentNoteEditorBinding? = null
    private val binding: FragmentNoteEditorBinding
        get() = _binding ?: throw Exception(
            "FragmentNoteEditorBinding is null"
        )

    private val args by navArgs<NoteEditorFragmentArgs>()
    private val noteId by lazy {
        args.noteId
    }

    private var isSaving = false

    private val viewModel by lazy {
        ViewModelProvider(this)[NotesEditorViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNote()
        launchRightMode()
        configureToolBar()
    }

    private fun configureToolBar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onPause() {
        super.onPause()

        if (requireActivity().isChangingConfigurations) {
            Log.d(LOG_TAG, "ConfigurationChangingCaught")
            return
        }
        if (!isSaving) { // При сохранении фрагмента через кнопку он закрывается. Будет вызван
            // onPause Чтобы не сохранять его как черновик, добавлена эта проверка
            Log.d(LOG_TAG, "onPause")
            viewLifecycleOwner.lifecycleScope.launch {
                val inputTitle = binding.etTitle.text.toString()
                val inputText = binding.etText.text.toString()
                if (isNewNote()) {
                    Log.d(LOG_TAG, "isNewNote")
                    viewModel.createNewNoteAndSaveToDb(true, inputTitle, inputText)
                } else {
                    Log.d(LOG_TAG, "notNewNote")
                    viewModel.editNote(true, inputTitle, inputText)
                }
            }
        }
        Log.d(LOG_TAG, "onPauseFinish")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchRightMode() {
        if (isNewNote()) {
            launchCreateMode(false)
        } else {
            launchEditMode(false)
        }
    }

    private fun launchCreateMode(isDraftNote: Boolean) {
        binding.buttonSaveNote.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val inputTitle = binding.etTitle.text.toString()
                val inputText = binding.etText.text.toString()
                isSaving = true
                viewModel.createNewNoteAndSaveToDb(isDraftNote, inputTitle, inputText)
            }
        }
    }

    private fun launchEditMode(isDraftNote: Boolean) {
        viewModel.getNote(noteId)
        binding.buttonSaveNote.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val inputTitle = binding.etTitle.text.toString()
                val inputText = binding.etText.text.toString()
                isSaving = true
                viewModel.editNote(isDraftNote, inputTitle, inputText)
            }
        }
    }

    private fun isNewNote(): Boolean {
        return when (noteId) {
            Note.UNDEFINED_ID -> true
            else -> false
        }
    }

    private fun observeNote() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.note.collect { state ->
                    when (state) {
                        is NotesEditorState.Initial -> {

                        }

                        is NotesEditorState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is NotesEditorState.Open -> {
                            with(binding) {
                                progressBar.visibility = View.GONE
                                val note = state.note
                                etTitle.setText(note.title)
                                etText.setText(note.text)
                            }
                        }

                        is NotesEditorState.Close -> {
                            findNavController().navigateUp()
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val LOG_TAG = "NoteEditorFragment"
    }
}