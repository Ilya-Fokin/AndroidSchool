package com.example.lesson_4_fokin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_4_fokin.databinding.BaseItemBinding

class BaseCardViewHolder(
    parent: ViewGroup,
    private val baseListener: BaseCardListener,
    private val detailListener: DetailCardListener
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.base_item, parent, false),
) {
    private val binding by viewBinding(BaseItemBinding::bind)
    fun bind(item: BaseCard) = with(binding) {
        root.setOnClickListener {
            baseListener.onBaseCardClick(item)
        }
        imgCardView.setImageResource(item.imgResourceId)
        titleInfoInCard.text = item.title
        detailInfoInCard.visibility= View.GONE
    }
    fun bind(item: DetailCard) = with(binding) {
        root.setOnClickListener {
            detailListener.onDetailCardClick(item)
        }
        imgCardView.setImageResource(item.imgResourceId)
        titleInfoInCard.text = item.title
        detailInfoInCard.text = item.detail
        detailInfoInCard.visibility= View.VISIBLE
    }
}