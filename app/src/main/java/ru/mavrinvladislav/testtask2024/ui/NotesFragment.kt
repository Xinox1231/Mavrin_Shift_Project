package ru.mavrinvladislav.testtask2024.ui

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.mavrinvladislav.testtask2024.databinding.FragmentNotesBinding
import ru.mavrinvladislav.testtask2024.presentation.NotesStateScreen
import ru.mavrinvladislav.testtask2024.presentation.NotesViewModel
import ru.mavrinvladislav.testtask2024.presentation.adapter.NotesAdapter


class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding: FragmentNotesBinding
        get() = _binding ?: throw Exception(
            "FragmentNotesBinding is null"
        )
    private val viewModel by lazy {
        ViewModelProvider(this)[NotesViewModel::class.java]
    }

    private val adapter by lazy {
        NotesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        observeViewModel()
        binding.rcViewNotes.adapter = adapter
        adapter.onNoteClickListener = { note ->
            launchNoteEditorFragmentEdit(note.id)
        }

    }

    private fun onLongNoteClick() {
        val vibrator = requireContext().getSystemService(Vibrator::class.java)

        adapter.onNoteLongClickListener = { note ->
            if (vibrator.hasVibrator()) {
                Toast.makeText(requireContext(), note.title, Toast.LENGTH_SHORT).show()
                // Для API 26 и выше
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val effect =
                        VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
                    vibrator.vibrate(effect)
                } else {
                    // Для более старых версий Android
                    vibrator.vibrate(500)
                }
            }
            viewModel.deleteNote(note)
            true // Возвращаем true, чтобы подтвердить обработку события
        }
    }

    private fun setupClickListeners() {
        binding.floatingButtonAddNote.setOnClickListener {
            launchNoteEditorFragmentAdd()
        }
        onLongNoteClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(LOG_TAG, "onDestroyView")
        _binding = null
    }

    private fun launchNoteEditorFragmentEdit(noteId: Int) {
        findNavController().navigate(
            NotesFragmentDirections.actionNotesFragmentToNoteEditorFragment(
                noteId
            )
        )
    }

    private fun launchNoteEditorFragmentAdd() {
        findNavController().navigate(
            NotesFragmentDirections.actionNotesFragmentToNoteEditorFragment()
        )
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    when (it) {
                        NotesStateScreen.NoContent -> {
                            binding.ivNoContent.visibility = View.VISIBLE
                            binding.tvNoContent.visibility = View.VISIBLE
                        }

                        is NotesStateScreen.ContentLoaded -> {
                            binding.ivNoContent.visibility = View.GONE
                            binding.tvNoContent.visibility = View.GONE
                            adapter.submitList(it.list)
                        }
                    }
                }
            }

        }
    }

    companion object {
        private const val LOG_TAG = "NotesFragment"
    }
}