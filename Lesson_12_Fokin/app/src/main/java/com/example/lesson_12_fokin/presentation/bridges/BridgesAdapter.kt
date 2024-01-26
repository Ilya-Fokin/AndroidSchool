package com.example.lesson_12_fokin.presentation.bridges

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson_12_fokin.data.remote.model.Bridge

class BridgesAdapter : RecyclerView.Adapter<BridgeViewHolder>() {
    lateinit var bridgeCardListener: BridgeCardListener
    private val items = mutableListOf<Bridge>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BridgeViewHolder {
        return BridgeViewHolder(parent, bridgeCardListener)
    }

    override fun onBindViewHolder(holder: BridgeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(bridges: List<Bridge>) {
        items.clear()
        items.addAll(bridges)
        notifyDataSetChanged()
    }
}