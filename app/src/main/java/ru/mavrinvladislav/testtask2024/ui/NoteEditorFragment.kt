package ru.mavrinvladislav.testtask2024.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import ru.mavrinvladislav.testtask2024.R
import ru.mavrinvladislav.testtask2024.databinding.FragmentNoteEditorBinding
import ru.mavrinvladislav.testtask2024.domain.Note
import ru.mavrinvladislav.testtask2024.presentation.NoteApplication
import ru.mavrinvladislav.testtask2024.presentation.NotesEditorState
import ru.mavrinvladislav.testtask2024.presentation.NoteEditorViewModel
import ru.mavrinvladislav.testtask2024.presentation.ViewModelFactory
import ru.mavrinvladislav.testtask2024.utils.Constants
import javax.inject.Inject

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


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: NoteEditorViewModel

    private val component by lazy {
        (requireActivity().application as NoteApplication).component
    }

    override fun onAttach(context: Context) {
        component.inject(this@NoteEditorFragment)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        viewModel = ViewModelProvider(this, viewModelFactory)[NoteEditorViewModel::class]
        observeNote()
        launchRightMode()
        configureToolBar()

    }

    override fun onPause() {
        super.onPause()

        if (requireActivity().isChangingConfigurations) {
            Log.d(LOG_TAG, "ConfigurationChangingCaught")
            return
        }
        if (!viewModel.shouldSkipSaveOnPause) { // При сохранении фрагмента через кнопку он закрывается. Будет вызван
            // onPause Чтобы не сохранять его как черновик, добавлена эта проверка
            Log.d(LOG_TAG, "onPause")
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
        Log.d(LOG_TAG, "onPauseFinish")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.deleteNote -> {
                viewModel.deleteNote(noteId)
                true
            }

            R.id.pinNote -> {
                // Логика для прикрепления заметки
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun configureToolBar() {
        val toolbar = binding.toolbar
        toolbar.title = Constants.EMPTY_STRING
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
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