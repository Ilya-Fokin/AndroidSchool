package com.example.lesson_11_fokin.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_11_fokin.R
import com.example.lesson_11_fokin.databinding.ActivityMainBinding
import com.example.lesson_11_fokin.view.ChartView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding.chartView.setData((1..9).map { Random.nextInt(15, 100) })
    }
}