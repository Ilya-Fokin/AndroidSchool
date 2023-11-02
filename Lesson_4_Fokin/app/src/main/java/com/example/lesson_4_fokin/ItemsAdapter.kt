package com.example.lesson_4_fokin

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

private const val TYPE_DETAIL = 0
private const val TYPE_BASE = 1

class ItemsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<ListItem>()
    lateinit var detailListener: DetailCardListener
    lateinit var baseListener: BaseCardListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_DETAIL -> DetailCardViewHolder(parent, detailListener)
            TYPE_BASE -> BaseCardViewHolder(parent, baseListener, detailListener)
            else -> error("ViewType not supported")
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BaseCardViewHolder && getDefaultType(position) == TYPE_DETAIL) {
            holder.bind((items[position] as ListItem.DetailItem).detailCard)
        } else
            if (holder is BaseCardViewHolder && getDefaultType(position) == TYPE_BASE) {
                holder.bind((items[position] as ListItem.BaseItem).baseCard)
            } else
                if (holder is DetailCardViewHolder) {
                    holder.bind((items[position] as ListItem.DetailItem).detailCard)
                }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is ListItem.DetailItem && isLastTypeInList(position) && !isParityItems(position)) {
            TYPE_BASE
        } else
        if (items[position] is ListItem.DetailItem) {
                TYPE_DETAIL
            } else {
            TYPE_BASE
        }
    }

    private fun getDefaultType(position: Int): Int {
        return if (items[position] is ListItem.DetailItem) {
            TYPE_DETAIL
        } else {
            TYPE_BASE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(items: List<ListItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    private fun isLastTypeInList(position: Int) : Boolean {
        for (i in position+1 until items.size) {
            if (getDefaultType(i) == getDefaultType(position)) {
                return false
            }
        }
        return true
    }

    private fun isParityItems(position: Int) : Boolean {
        val type = getDefaultType(position)
        var counter: Int = 0
        repeat(items.size) {index ->
            if (getDefaultType(index) == type) {
                counter++
            }
        }
        return counter % 2 == 0
    }
}