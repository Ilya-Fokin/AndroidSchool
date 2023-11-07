package com.example.lesson_5_fokin

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.lesson_5_fokin.databinding.ItemCardBinding

class ItemCardViewHolder(
    parent: ViewGroup,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
) {

    private val binding by viewBinding(ItemCardBinding::bind)

    fun bind(item: ItemCard) = with(binding) {
        root.setOnClickListener {
            Toast.makeText(root.context, item.title,Toast.LENGTH_SHORT).show()
        }
        imageButton.setOnClickListener {
            Toast.makeText(root.context, "Тык на кнопку",Toast.LENGTH_SHORT).show()
        }
        Glide.with(root.context)
            .load(item.imgUri)
            .into(imageViewItem)
        textViewCompanyName.text = item.companyName
        textViewTitle.text= item.title
        textViewAddress.text = item.address
    }
}