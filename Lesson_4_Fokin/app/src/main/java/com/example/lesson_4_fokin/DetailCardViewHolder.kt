package com.example.lesson_4_fokin

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_4_fokin.databinding.ActivityMainBinding
import com.example.lesson_4_fokin.databinding.BaseItemBinding
import com.example.lesson_4_fokin.databinding.DetailItemBinding

class DetailCardViewHolder(
    private val parent: ViewGroup,
    private val detailListener: DetailCardListener
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.detail_item, parent, false),
) {
    private val binding by viewBinding(DetailItemBinding::bind)

    fun bind(item: DetailCard) = with(binding) {
        root.setOnClickListener {
            detailListener.onDetailCardClick(item)
        }
        binding.imgCardView.setImageResource(item.imgResourceId)
        titleInfoInCard.text = item.title
        if (item.title == "Квитанции" || item.title == "Счётчики") {
            detailInfoInCard.setTextColor(ContextCompat.getColor(root.context, R.color.red))
            detailInfoInCard.text = item.detail
        } else
            detailInfoInCard.text = item.detail
    }
}