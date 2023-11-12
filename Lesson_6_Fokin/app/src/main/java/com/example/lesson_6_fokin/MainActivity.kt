package com.example.lesson_6_fokin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_6_fokin.fragments.FirstFragment
import com.example.lesson_6_fokin.fragments.SecondFragment
import com.example.lesson_6_fokin.fragments.ThirdFragment
import com.example.lesson_6_fokin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firstFragment = FirstFragment()
        val secondFragment = SecondFragment()
        val thirdFragment = ThirdFragment()

        setFragment(firstFragment)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.one -> setFragment(firstFragment)
                R.id.two -> setFragment(secondFragment)
                R.id.three -> setFragment(thirdFragment)
            }
            true
        }
    }
    private fun setFragment(fragment: Fragment) = supportFragmentManager.beginTransaction()
        .replace(R.id.fragmentLayout, fragment)
            .commit()
}