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
import com.example.lesson_8_fokin.databinding.FragmentFirstBinding
import com.example.lesson_8_fokin.model.NoteListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import kotlinx.coroutines.launch


class FirstFragment : Fragment(R.layout.fragment_first) {

    private val binding by viewBinding(FragmentFirstBinding::bind)
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
            onClickListener = NoteListener {
                (activity as? NavigationController)?.navigate(SecondFragment.newInstance(it))
            }

            onLongClickListener = NoteListener {
                val note = it
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
                    deleteNote(note)
                    dialog.cancel()
                }

                customLayout.findViewById<Button>(R.id.buttonArchive).setOnClickListener {
                    setInArchiveNote(note)
                    dialog.cancel()
                }
            }
        }

        binding.fab.setOnClickListener {
            (activity as? NavigationController)?.navigate(SecondFragment.newInstance(null))
        }
    }

    private fun loadNotes() {
        lifecycleScope.launch {
            binding.progressBar.isVisible = true
            try {
                DatabaseClient.noteDao(requireContext()).getNotesFlow().collect { notes ->
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
                error(ex.message.toString())
            }
        }
    }

    private fun searchNote(title: String) {
        lifecycleScope.launch {
            binding.progressBar.isVisible = true
            binding.textViewError.isVisible = false
            try {
                DatabaseClient.noteDao(requireContext()).searchNotesFlowByName(title)
                    .collect() { notes ->
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
                error(ex.message.toString())
            }
        }
    }

    private fun deleteNote(note: NoteEntity) {
        lifecycleScope.launch {
            try {
                DatabaseClient.noteDao(requireContext()).deleteById(note.id)
            } catch (ex: Exception) {
                error(ex.message.toString())
            }
        }
    }

    private fun setInArchiveNote(note: NoteEntity) {
        lifecycleScope.launch {
            try {
                DatabaseClient.noteDao(requireContext()).addInArchive(note.id)
            } catch (ex: Exception) {
                error(ex.message.toString())
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

    companion object {
        fun newInstance(): FirstFragment {
            return FirstFragment()
        }
    }
}