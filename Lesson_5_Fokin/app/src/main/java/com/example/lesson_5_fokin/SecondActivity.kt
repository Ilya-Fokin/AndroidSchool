package com.example.lesson_5_fokin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_5_fokin.databinding.FirstActivityBinding
import com.example.lesson_5_fokin.databinding.SecondActivityBinding

class SecondActivity : AppCompatActivity() {
    private val binding by viewBinding(SecondActivityBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)

        binding.goToThirdButton.setOnClickListener {
            startActivity(
                Intent(this, ThirdActivity::class.java)
            )
        }
    }
}