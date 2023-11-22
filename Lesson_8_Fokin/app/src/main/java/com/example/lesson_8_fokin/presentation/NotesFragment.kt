package com.example.lesson_8_fokin.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_8_fokin.R
import com.example.lesson_8_fokin.data.DatabaseClient
import com.example.lesson_8_fokin.data.entity.NoteEntity
import com.example.lesson_8_fokin.databinding.FragmentNotesBinding
import com.example.lesson_8_fokin.model.NoteListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import kotlinx.coroutines.launch

class NotesFragment : Fragment(R.layout.fragment_notes) {

    private val binding by viewBinding(FragmentNotesBinding::bind)
    private val notesAdapter = NoteAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadNotes()

        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.recyclerView.layoutManager = staggeredGridLayoutManager

        binding.recyclerView.addItemDecoration(
            StaggeredGridItemDecoration(
                requireContext().resources.getDimensionPixelSize(
                    R.dimen.default_card_indent
                )
            )
        )

        binding.recyclerView.adapter = notesAdapter.apply {
            onClickListener = object : NoteListener {

                override fun onClick(item: NoteEntity) {
                    (activity as? NavigationController)?.navigate(EditNoteFragment.newInstance(item))
                }

                override fun onLongClick(item: NoteEntity) {
                    val builder = MaterialAlertDialogBuilder(
                        requireContext(),
                        R.style.MaterialAlertDialog_rounded
                    )
                    val customLayout: View = layoutInflater.inflate(R.layout.dialog_menu, null)

                    builder.setView(customLayout)
                        .setNegativeButton(context?.getText(R.string.cancel)) { dialog, _ ->
                            dialog.cancel()
                        }

                    val dialog = builder.create()
                    dialog.show()

                    customLayout.findViewById<Button>(R.id.buttonRemove).setOnClickListener {
                        deleteNote(item)
                        dialog.cancel()
                    }

                    customLayout.findViewById<Button>(R.id.buttonArchive).setOnClickListener {
                        setInArchiveNote(item)
                        dialog.cancel()
                    }
                }
            }
        }

        binding.fab.setOnClickListener {
            (activity as? NavigationController)?.navigate(EditNoteFragment.newInstance(null))
        }
    }

    private fun loadNotes() {
        lifecycleScope.launch {
            binding.progressBar.isVisible = true
            try {
                DatabaseClient.noteDao(requireContext()).getNotesFlow().collect { notes ->
                    binding.textViewError.isVisible = false
                    if (notes.isEmpty()) {
                        notesAdapter.setNotes(listOf())
                        binding.progressBar.isVisible = false
                        binding.textViewError.text =
                            context?.resources?.getText(R.string.empty_note)
                        binding.textViewError.isVisible = true
                    } else {
                        binding.progressBar.isVisible = false
                        notesAdapter.setNotes(notes)
                    }
                }
            } catch (ex: Exception) {
                binding.textViewError.text = ex.message
                binding.textViewError.isVisible = true
            }
        }
    }

    private fun searchNote(title: String) {
        lifecycleScope.launch {
            binding.progressBar.isVisible = true
            try {
                DatabaseClient.noteDao(requireContext()).searchNotesFlowByName(title)
                    .collect() { notes ->
                        binding.textViewError.isVisible = false
                        if (notes.isEmpty()) {
                            binding.progressBar.isVisible = false
                            binding.textViewError.text =
                                context?.resources?.getText(R.string.empty_note)
                            binding.textViewError.isVisible = true
                        } else {
                            binding.progressBar.isVisible = false
                            notesAdapter.setNotes(notes)
                        }
                    }
            } catch (ex: Exception) {
                binding.textViewError.text = ex.message
                binding.textViewError.isVisible = true
            }
        }
    }

    private fun deleteNote(note: NoteEntity) {
        lifecycleScope.launch {
            try {
                DatabaseClient.noteDao(requireContext()).deleteById(note.id)
            } catch (ex: Exception) {
                binding.textViewError.text = ex.message
                binding.textViewError.isVisible = true
            }
        }
    }

    private fun setInArchiveNote(note: NoteEntity) {
        lifecycleScope.launch {
            try {
                DatabaseClient.noteDao(requireContext()).addInArchive(note.id)
            } catch (ex: Exception) {
                binding.textViewError.text = ex.message
                binding.textViewError.isVisible = true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_search, menu)
        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                notesAdapter.setNotes(listOf())
                if (!searchView.isIconified) {
                    searchView.isIconified = true
                }
                searchNote(query)
                menuItem.collapseActionView()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                notesAdapter.setNotes(listOf())
                searchNote(newText.toString())
                return false
            }
        })
    }

    fun searchMenu() {
        requireActivity().menuInflater.inflate(R.menu.menu_search, binding.toolbar.menu)
        val menuItem = binding.toolbar.menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                notesAdapter.setNotes(listOf())
                if (!searchView.isIconified) {
                    searchView.isIconified = true
                }
                searchNote(query)
                menuItem.collapseActionView()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                notesAdapter.setNotes(listOf())
                searchNote(newText.toString())
                return false
            }
        })
    }

    companion object {
        fun newInstance(): NotesFragment {
            return NotesFragment()
        }
    }
}