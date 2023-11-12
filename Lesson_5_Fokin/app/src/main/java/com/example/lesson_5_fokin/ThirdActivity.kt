package com.example.lesson_5_fokin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_5_fokin.databinding.FirstActivityBinding
import com.example.lesson_5_fokin.databinding.ThirdActivityBinding
import com.google.android.material.snackbar.Snackbar

class ThirdActivity : AppCompatActivity() {
    private val launcher = registerForActivityResult(
        FifthActivityContract()
    ) { result ->
        Snackbar.make(binding.root, result, Snackbar.LENGTH_SHORT).show()
    }

    private val binding by viewBinding(ThirdActivityBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.third_activity)

        binding.goToFirstButton.setOnClickListener {
            startActivity(
                Intent(this, FirstActivity::class.java)
            )
        }
        binding.goToFifthButton.setOnClickListener {
            launcher.launch(Unit)
        }
    }
}