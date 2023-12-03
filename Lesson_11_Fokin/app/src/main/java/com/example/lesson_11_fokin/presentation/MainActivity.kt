package com.example.lesson_11_fokin.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.bind
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_11_fokin.R
import com.example.lesson_11_fokin.databinding.ActivityMainBinding
import com.example.lesson_11_fokin.model.Data
import com.example.lesson_11_fokin.view.ChartView
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val formatter = DateTimeFormatter.ofPattern("dd.MM")

        binding.chartView.setData((9 downTo 1).map {
            Data(Random.nextInt(15, 100), LocalDate.now().minusDays(it.toLong()).format(formatter))
        })

        binding.root.postDelayed({
            binding.chartView.setData((9 downTo 1).map {
                Data(
                    Random.nextInt(15, 100),
                    LocalDate.now().minusDays(it.toLong()).format(formatter)
                )
            })
        }, 4000)
    }
}