package com.example.lesson_6_fokin.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_6_fokin.CatCard
import com.example.lesson_6_fokin.R
import com.example.lesson_6_fokin.ViewPagerAdapter
import com.example.lesson_6_fokin.databinding.ThirdFragmentBinding

class ThirdFragment : Fragment(R.layout.third_fragment) {

    private val binding by viewBinding(ThirdFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerFragment = PagerFragment()

        var showBanners = false

        binding.materialButton.setOnClickListener {
            if (!showBanners) {
                binding.materialButton.text = resources.getText(R.string.close_banner)
                setFragment(pagerFragment)
                showBanners = true
            } else {
                binding.materialButton.text = resources.getText(R.string.show_banner)
                removeFragment(pagerFragment)
                showBanners = false
            }
        }
    }

    private fun setFragment(fragment: Fragment) = childFragmentManager.beginTransaction()
        .replace(R.id.fragmentLayout, fragment)
            .commit()

    private fun removeFragment(fragment: Fragment) = childFragmentManager.beginTransaction()
        .remove(fragment)
            .commit()
}