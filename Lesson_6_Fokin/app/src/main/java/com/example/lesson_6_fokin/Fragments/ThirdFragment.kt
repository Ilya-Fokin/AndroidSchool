package com.example.lesson_6_fokin.Fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_6_fokin.CatCard
import com.example.lesson_6_fokin.R
import com.example.lesson_6_fokin.ViewPagerAdapter
import com.example.lesson_6_fokin.databinding.ThirdFragmentBinding

class ThirdFragment : Fragment(R.layout.third_fragment) {

    private val binding by viewBinding(ThirdFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ViewPagerAdapter()
        var showBanners = false

        binding.materialButton.setOnClickListener {
            if (!showBanners) {
                adapter.setList(
                    listOf(
                        CatCard(
                            "Картинка 1",
                            R.drawable.img_zalepuha
                        ),
                        CatCard(
                            "Картинка 2",
                            R.drawable.img_zalepuha2
                        ),
                        CatCard(
                            "Картинка 3",
                            R.drawable.img_zalepuha3
                        )
                    )
                )
                binding.materialButton.text = "Свернуть баннер"
                binding.pager.adapter = adapter
                binding.dot1.attachTo(binding.pager)
                binding.dot1.visibility = View.VISIBLE
                showBanners = true
            } else {
                binding.materialButton.text = "Показать баннер"
                adapter.clear()
                binding.dot1.visibility = View.GONE
                binding.pager.adapter = adapter
                showBanners = false
            }
        }
    }
}