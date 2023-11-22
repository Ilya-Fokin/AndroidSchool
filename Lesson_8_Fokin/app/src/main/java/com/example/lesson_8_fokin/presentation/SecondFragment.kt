package com.example.lesson_8_fokin.presentation

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_8_fokin.R
import com.example.lesson_8_fokin.data.DatabaseClient
import com.example.lesson_8_fokin.data.entity.NoteEntity
import com.example.lesson_8_fokin.databinding.DialogColorsBinding
import com.example.lesson_8_fokin.databinding.FragmentSecondBinding
import com.example.lesson_8_fokin.model.SelectorColor
import com.example.lesson_8_fokin.model.SelectorColorListener
import com.example.lesson_8_fokin.model.SelectorStatus
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.zip.Inflater


class SecondFragment : Fragment(R.layout.fragment_second) {

    private val binding by viewBinding(FragmentSecondBinding::bind)
    private val selectorAdapter = SelectorColorAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var selectorColor = SelectorColor(R.color.white, SelectorStatus.CHECKED)

        var note = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(NOTE_KEY, NoteEntity::class.java)
        } else {
            arguments?.getParcelable(NOTE_KEY)
        }

        if (note != null) {
            binding.editTextTitle.setText(note.title)
            binding.editTextNote.setText(note.description)
            selectorColor = SelectorColor(note.color, SelectorStatus.CHECKED)
        }

        binding.toolbar.setNavigationOnClickListener {
            if (note == null) {
                if (binding.editTextNote.text.isNotEmpty()) {
                    val newNote = NoteEntity(
                        title = binding.editTextTitle.text.toString(),
                        description = binding.editTextNote.text.toString(),
                        color = selectorColor.color
                    )
                    saveNote(newNote)
                }
            } else {
                note.title = binding.editTextTitle.text.toString()
                note.description = binding.editTextNote.text.toString()
                note.color = selectorColor.color
                updateNote(note)
            }
            parentFragmentManager.popBackStack()
        }

        binding.toolbar.menu.findItem(R.id.menuSelectColor).setOnMenuItemClickListener {
            val builder =
                MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_rounded)
            val customLayout: View = layoutInflater.inflate(R.layout.dialog_colors, null)

            builder.setTitle(context?.getText(R.string.color_selection))
            builder.setView(customLayout)
                .setNegativeButton(context?.getText(R.string.cancel)) { dialog, _ ->
                    dialog.cancel()
                }

            val layoutManager = GridLayoutManager(this.context, 4)
            val recyclerView = customLayout.findViewById<RecyclerView>(R.id.recyclerView)

            recyclerView.addItemDecoration(
                GridItemDecoration(
                    4,
                    resources.getDimensionPixelSize(R.dimen.default_card_indent),
                    true
                )
            )

            recyclerView.layoutManager = layoutManager
            loadColors(selectorColor.color)

            val dialog = builder.create()

            recyclerView.adapter = selectorAdapter.apply {
                selectorColorListener = SelectorColorListener {
                    val index1 = getPositionItem(selectorColor.color)
                    val index2: Int

                    selectorColor = if (selectorColor.color == it.color) {
                        removeSelector()
                        selectorAdapter.notifyItemChanged(index1)
                        SelectorColor(R.color.white, SelectorStatus.CHECKED)
                    } else if (selectorColor.color != R.color.white) {
                        replaceSelector(it.color)
                        selectorAdapter.notifyItemChanged(index1)
                        index2 = getPositionItem(it.color)
                        selectorAdapter.notifyItemChanged(index2)
                        SelectorColor(it.color, SelectorStatus.CHECKED)
                    } else {
                        replaceSelector(it.color)
                        index2 = getPositionItem(it.color)
                        selectorAdapter.notifyItemChanged(index2)
                        SelectorColor(it.color, SelectorStatus.CHECKED)
                    }
                    dialog.cancel()
                }
            }
            dialog.show()

            true
        }
    }

    private fun saveNote(note: NoteEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                DatabaseClient.noteDao(requireContext()).save(
                    NoteEntity(
                        id = note.id,
                        title = note.title,
                        description = note.description,
                        color = note.color,
                        inArchive = note.inArchive
                    )
                )
            } catch (ex: Exception) {
                error(ex.message.toString())
            }
        }
    }

    private fun updateNote(note: NoteEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                DatabaseClient.noteDao(requireContext()).update(
                    NoteEntity(
                        id = note.id,
                        title = note.title,
                        description = note.description,
                        color = note.color,
                        inArchive = note.inArchive
                    )
                )
            } catch (ex: Exception) {
                error(ex.message.toString())
            }
        }
    }

    private fun loadColors(color: Int) {
        val colors = mutableListOf(
            SelectorColor(R.color.red, SelectorStatus.UNCHECKED),
            SelectorColor(R.color.crimson, SelectorStatus.UNCHECKED),
            SelectorColor(R.color.purple, SelectorStatus.UNCHECKED),
            SelectorColor(R.color.dark_blue, SelectorStatus.UNCHECKED),
            SelectorColor(R.color.blue, SelectorStatus.UNCHECKED),
            SelectorColor(R.color.light_blue, SelectorStatus.UNCHECKED),
            SelectorColor(R.color.navy_blue, SelectorStatus.UNCHECKED),
            SelectorColor(R.color.turquoise, SelectorStatus.UNCHECKED),
            SelectorColor(R.color.green, SelectorStatus.UNCHECKED),
            SelectorColor(R.color.light_green, SelectorStatus.UNCHECKED),
            SelectorColor(R.color.orange, SelectorStatus.UNCHECKED),
            SelectorColor(R.color.dark_orange, SelectorStatus.UNCHECKED),
            SelectorColor(R.color.grey, SelectorStatus.UNCHECKED),
            SelectorColor(R.color.dark_grey_variant, SelectorStatus.UNCHECKED)
        )
        repeat(colors.size) {
            if (color == colors[it].color) {
                colors[it] = SelectorColor(color, SelectorStatus.CHECKED)
            }
        }
        selectorAdapter.setSelectors(colors)
    }

    companion object {
        private const val NOTE_KEY = "note_key"

        fun newInstance(note: NoteEntity?): SecondFragment {
            return SecondFragment().also {
                it.arguments = Bundle().apply {
                    putParcelable(NOTE_KEY, note)
                }
            }
        }
    }
}