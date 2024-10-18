package ru.mavrinvladislav.testtask2024.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import ru.mavrinvladislav.testtask2024.databinding.FragmentNoteEditorBinding
import ru.mavrinvladislav.testtask2024.domain.Note
import ru.mavrinvladislav.testtask2024.presentation.NotesEditorViewModel
import ru.mavrinvladislav.testtask2024.utils.Constants

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
    }

    override fun onPause() {
        super.onPause()
        viewLifecycleOwner.lifecycleScope.launch {
            val title = binding.etTitle.text.toString()
            val text = binding.etText.text.toString()
        }
    }

    private fun launchRightMode() {
        if (isNewNote()) {
            launchCreateMode()
        } else {
            launchEditMode()
        }
    }

    private fun launchCreateMode() {
        binding.buttonSaveNote.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val inputTitle = binding.etTitle.text.toString()
                val inputText = binding.etText.text.toString()
                viewModel.createNewNoteAndSaveToDb(inputTitle, inputText)
            }
        }
    }

    private fun launchEditMode() {
        viewModel.getNote(noteId)
        binding.buttonSaveNote.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val inputTitle = binding.etTitle.text.toString()
                val inputText = binding.etText.text.toString()
                viewModel.editNote(inputTitle, inputText)
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
        viewModel.note.observe(viewLifecycleOwner) { note ->
            with(binding) {
                etTitle.setText(note.title)
                etText.setText(note.text)
            }
        }
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigateUp()
            }
        }
    }

    companion object {
        private const val LOG_TAG = "NoteEditorFragment"
    }
}