package com.example.lesson_6_fokin.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_6_fokin.CatCard
import com.example.lesson_6_fokin.R
import com.example.lesson_6_fokin.ViewPagerAdapter
import com.example.lesson_6_fokin.databinding.PagerFragmentBinding

class PagerFragment : Fragment(R.layout.pager_fragment) {

    private val binding by viewBinding(PagerFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ViewPagerAdapter()
        binding.pager.adapter = adapter
        binding.dot1.attachTo(binding.pager)

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
    }
}