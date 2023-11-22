package com.example.lesson_8_fokin.presentation

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_8_fokin.R
import com.example.lesson_8_fokin.databinding.ItemSelectBinding
import com.example.lesson_8_fokin.model.SelectorColor
import com.example.lesson_8_fokin.model.SelectorColorListener
import com.example.lesson_8_fokin.model.SelectorStatus

class SelectorColorViewHolder(
    private val parent: ViewGroup,
    private val selectorColorListener: SelectorColorListener,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_select, parent, false)
) {
    private val binding by viewBinding(ItemSelectBinding::bind)

    fun bind(item: SelectorColor) = with(binding) {
        root.setOnClickListener {
            selectorColorListener.onClick(item)
        }
        selectorColor.setBackgroundResource(item.color)
        when (item.status) {
            SelectorStatus.CHECKED -> selectorColor.setImageResource(R.drawable.checked)
            SelectorStatus.UNCHECKED -> selectorColor.setImageResource(0)
        }
    }
}