package com.example.lesson_6_fokin

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson_6_fokin.viewHolders.PagerViewHolder

class ViewPagerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<CatCard>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PagerViewHolder(parent)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = holder.itemView.run {
        when (holder) {
            is PagerViewHolder -> holder.bind(items[position])
        }
    }

    fun clear() {
        this.items.clear()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(items: List<CatCard>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}