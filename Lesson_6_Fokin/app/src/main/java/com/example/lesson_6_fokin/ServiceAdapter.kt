package com.example.lesson_6_fokin

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson_6_fokin.ViewHolders.ServiceCardViewHolder

private const val TYPE_SERVICE = 0

class ServiceAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<ListItem>()
    lateinit var itemListener: ServiceCardListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_SERVICE -> ServiceCardViewHolder(parent, itemListener)
            else -> error("ViewType not supported")
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ServiceCardViewHolder -> holder.bind((items[position] as ListItem.ServiceItem).item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is ListItem.ServiceItem) {
            TYPE_SERVICE
        } else {
            error("ViewType not supported")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(items: List<ListItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}