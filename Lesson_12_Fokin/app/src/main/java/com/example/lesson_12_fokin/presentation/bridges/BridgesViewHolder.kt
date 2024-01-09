package com.example.lesson_12_fokin.presentation.bridges

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_12_fokin.R
import com.example.lesson_12_fokin.data.remote.model.Bridge
import com.example.lesson_12_fokin.databinding.ItemBridgeBinding

class BridgeViewHolder(
    parent: ViewGroup,
    private val bridgeCardListener: BridgeCardListener
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_bridge, parent, false)
) {
    private val binding by viewBinding(ItemBridgeBinding::bind)

    fun bind(item: Bridge) = with(binding) {
        root.setOnClickListener {
            bridgeCardListener.setOnClickCard(item)
        }
        root.bind(item)
    }
}