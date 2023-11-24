package com.example.lesson_8_fokin.presentation

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson_8_fokin.data.entity.NoteEntity
import com.example.lesson_8_fokin.model.SelectorColor
import com.example.lesson_8_fokin.model.SelectorColorListener
import com.example.lesson_8_fokin.model.SelectorStatus

class SelectorColorAdapter : RecyclerView.Adapter<SelectorColorViewHolder>() {

    lateinit var selectorColorListener: SelectorColorListener
    private val selectors = mutableListOf<SelectorColor>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectorColorViewHolder {
        return SelectorColorViewHolder(parent, selectorColorListener)
    }

    override fun getItemCount() = selectors.size

    override fun onBindViewHolder(holder: SelectorColorViewHolder, position: Int) {
        holder.bind(selectors[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectors(selector: List<SelectorColor>) {
        this.selectors.clear()
        this.selectors.addAll(selector)
        notifyDataSetChanged()
    }

    fun replaceSelector(newColor: Int) {
        repeat(selectors.size) { index ->
            if (selectors[index].status == SelectorStatus.CHECKED) {
                selectors[index].status = SelectorStatus.UNCHECKED
            }
            if (selectors[index].color == newColor) {
                selectors[index].status = SelectorStatus.CHECKED
            }
        }
    }

    fun removeSelector() {
        repeat(selectors.size) { index ->
            if (selectors[index].status == SelectorStatus.CHECKED) {
                selectors[index].status = SelectorStatus.UNCHECKED
            }
        }
    }

    fun getPositionItem(color: Int): Int {
        repeat(selectors.size) { index ->
            if (selectors[index].color == color) {
                return index
            }
        }
        return -1
    }
}