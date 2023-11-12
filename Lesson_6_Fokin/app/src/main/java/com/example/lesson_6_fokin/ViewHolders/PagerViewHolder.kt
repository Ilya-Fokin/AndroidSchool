package com.example.lesson_6_fokin.ViewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_6_fokin.CatCard
import com.example.lesson_6_fokin.R
import com.example.lesson_6_fokin.databinding.ItemPagerBinding

class PagerViewHolder(
    private val parent: ViewGroup,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_pager, parent, false),
) {

    private val binding by viewBinding(ItemPagerBinding::bind)

    fun bind(item: CatCard) = with(binding) {
    root.setOnClickListener {
        Toast.makeText(root.context, item.title, Toast.LENGTH_SHORT).show()
    }
        binding.textViewCat.text = item.title
        binding.imageCat.setImageResource(item.imgResourceId)
    }
}