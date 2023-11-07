package com.example.lesson_5_fokin

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.lesson_5_fokin.databinding.SixthActivityBinding
import com.google.android.material.snackbar.Snackbar

class SixthActivity : AppCompatActivity() {

    private val binding by viewBinding(SixthActivityBinding::bind)

    private val itemsAdapter = ItemsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sixth_activity)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val customItemDecoration = CustomItemDecoration(12)
        binding.recycleViewItems.layoutManager = layoutManager
        binding.recycleViewItems.addItemDecoration(customItemDecoration)

        binding.recycleViewItems.adapter = itemsAdapter

        binding.buttonPartnership.setOnClickListener {
            Toast.makeText(this, "Вы предложили услугу", Toast.LENGTH_SHORT).show()
        }

        binding.textViewButton.setOnClickListener {
            Toast.makeText(this, "Все", Toast.LENGTH_SHORT).show()
        }

        Glide.with(this)
            .load("https://www.doverie-omsk.ru/upload/u39/wysiwyg/2020-nedvizhimost/sovrem-doma-vbud-13.jpg")
            .into(binding.imageViewBackgroundToolbar)
            .view.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY)

        itemsAdapter.setList(
            listOf(
                ListItem.Item(ItemCard(
                    "Скидка 10% на выпечку по коду",
                    "Царь пышка",
                    "Лермонтовский пр., д.10",
                    Uri.parse("https://i.pinimg.com/originals/03/41/0c/03410ce6ac1d7218940a8543c17bab1e.jpg"))),
                ListItem.Item(ItemCard(
                    "Скидка 5% на чистку пальто",
                    "Химчистка 'МАЙ?'",
                    "Лермонтовский пр., д.48",
                    Uri.parse("https://21.img.avito.st/432x324/4625434521.jpg"))),
                ListItem.Item(ItemCard(
                    "При покупке шавермы, фалафель бесплатно",
                    "Шаверма У Ашота",
                    "Лермонтовский пр., д.52",
                    Uri.parse("https://media.baamboozle.com/uploads/images/42852/1652361787_300392_url.jpeg")))
            )
        )
    }
}