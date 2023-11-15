package com.example.lesson_7_fokin.presentation

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson_7_fokin.data.model.Bridge
import com.example.lesson_7_fokin.data.model.BridgeCardListener

class BridgesAdapter : RecyclerView.Adapter<BridgesViewHolder>() {
    lateinit var bridgeCardListener: BridgeCardListener
    private val bridges = mutableListOf<Bridge>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BridgesViewHolder {
        return BridgesViewHolder(parent, bridgeCardListener)
    }

    override fun getItemCount() = bridges.size

    override fun onBindViewHolder(holder: BridgesViewHolder, position: Int) {
        holder.bind(bridges[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setBridges(bridges: List<Bridge>) {
        this.bridges.clear()
        this.bridges.addAll(bridges)
        notifyDataSetChanged()
    }
}