package com.example.lesson_6_fokin

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson_6_fokin.viewHolders.ServiceCardViewHolder

class ServiceAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Service>()
    lateinit var itemListener: ServiceCardListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ServiceCardViewHolder(parent, itemListener)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ServiceCardViewHolder -> holder.bind((items[position]))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(items: List<Service>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}