package com.example.lesson_6_fokin.ViewHolders

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_6_fokin.R
import com.example.lesson_6_fokin.Service
import com.example.lesson_6_fokin.ServiceCardListener
import com.example.lesson_6_fokin.databinding.ItemTwoCardBinding

class ServiceCardViewHolder(
    private val parent: ViewGroup,
    private val serviceListener: ServiceCardListener
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_two_card, parent, false),
) {

    private val binding by viewBinding(ItemTwoCardBinding::bind)

    fun bind(item: Service) = with(binding) {
        imageButtonEnter.setOnClickListener {
            serviceListener.onClickEnter(item)
        }
        imageButtonInfoInCard.setOnClickListener {
            Toast.makeText(root.context, "Тык на инфо", Toast.LENGTH_SHORT).show()
        }
        imageButtonMoreInCard.setOnClickListener {
            Toast.makeText(root.context, "Тык на more", Toast.LENGTH_SHORT).show()
        }
        binding.imageViewIconService.setImageResource(item.imgResourceId)
        textViewNameService.text = item.title
        textViewSerialNumber.text = item.serialNum.toString()
        if (item.title == "Электроэнергия") {
            binding.input1.root.layoutParams.width = 240
            binding.input1.nameInput.text = item.input[0]

            binding.input2.root.visibility = View.VISIBLE
            binding.input2.root.layoutParams.width = 240
            binding.input2.nameInput.text = item.input[1]

            binding.input3.root.visibility = View.VISIBLE
            binding.input3.root.layoutParams.width = 240
            binding.input3.nameInput.text = item.input[2]

            imageViewAlert.visibility = View.GONE

            textViewAlert.setTextColor(Color.BLACK)
            textViewAlert.text = item.alert
        } else {
            binding.input1.nameInput.text = item.input[0]
            textViewAlert.text= item.alert
        }
    }
}