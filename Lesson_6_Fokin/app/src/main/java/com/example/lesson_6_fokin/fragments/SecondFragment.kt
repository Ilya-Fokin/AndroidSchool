package com.example.lesson_6_fokin.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_6_fokin.CustomItemDecoration
import com.example.lesson_6_fokin.R
import com.example.lesson_6_fokin.Service
import com.example.lesson_6_fokin.ServiceAdapter
import com.example.lesson_6_fokin.ServiceCardListener
import com.example.lesson_6_fokin.ServiceType
import com.example.lesson_6_fokin.databinding.SecondFragmentBinding
import com.google.android.material.snackbar.Snackbar

class SecondFragment : Fragment(R.layout.second_fragment) {

    private val itemsAdapter = ServiceAdapter()
    private val binding by viewBinding(SecondFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val customItemDecoration =
            CustomItemDecoration(resources.getDimensionPixelSize(R.dimen.service_decoration_margin))
        binding.recycleViewItems.layoutManager = layoutManager
        binding.recycleViewItems.addItemDecoration(customItemDecoration)

        binding.recycleViewItems.adapter = itemsAdapter

        binding.recycleViewItems.adapter = itemsAdapter.apply {
            itemListener = ServiceCardListener {
                Snackbar.make(binding.root, "Данные переданы", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.materialToolbar.menu.findItem(R.id.menuButtonLamp).setOnMenuItemClickListener {
            Toast.makeText(binding.root.context, "Тык на лампу", Toast.LENGTH_SHORT).show()
            true
        }

        itemsAdapter.setList(
            listOf(
                Service(
                    "Холодная вода",
                    53135818,
                    arrayListOf("Новые показания"),
                    "Необходимо подать показания до 25.03.18",
                    R.drawable.ic_water_cold,
                    ServiceType.COLD_WATER
                ),
                Service(
                    "Горячая вода",
                    53135825,
                    arrayListOf("Новые показания"),
                    "Необходимо подать показания до 25.03.18",
                    R.drawable.ic_water_hot,
                    ServiceType.HOT_WATER
                ),
                Service(
                    "Электроэнергия",
                    53135515,
                    arrayListOf("День", "Ночь", "Пик"),
                    "Показания сданы 16.02.18 и будут учтены при следующем расчете 25.02.18",
                    R.drawable.ic_electro_copy,
                    ServiceType.ELECTRICITY
                )
            )
        )
    }
}