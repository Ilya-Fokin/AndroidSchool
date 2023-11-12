package com.example.lesson_5_fokin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_5_fokin.databinding.FirstActivityBinding

class FirstActivity : AppCompatActivity() {

    private val binding by viewBinding(FirstActivityBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_activity)

        binding.goToFourthButton.setOnClickListener {
            startActivity(
                FourthActivity.createStartIntent(
                    this,
                    System.currentTimeMillis()
                )
            )
        }
        binding.goToSecondButton.setOnClickListener {
            startActivity(
                Intent(this, SecondActivity::class.java)
            )
        }
    }
}