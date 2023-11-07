package com.example.lesson_5_fokin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_5_fokin.databinding.FourthActivityBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FourthActivity : AppCompatActivity() {

    private val binding by viewBinding(FourthActivityBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fourth_activity)

        binding.textViewThis.text = this.toString()

        val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault())
        binding.currentTimeTextView.text = dateFormat.format(Date(intent.getLongExtra(KEY_CURRENT_TIME, 0)))

        binding.goToFourthButton.setOnClickListener {
            startActivity(
                createStartIntent(
                    this,
                    System.currentTimeMillis()
                )
            )
        }
    }

    companion object {
        const val KEY_CURRENT_TIME = "key_current_time"

        fun createStartIntent(context: Context, input: Long): Intent {
            return Intent(context, FourthActivity::class.java).apply {
                putExtra(KEY_CURRENT_TIME, input)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault())
        if (intent != null) {
            binding.currentTimeTextView.text = dateFormat.format(Date(intent.getLongExtra(KEY_CURRENT_TIME, 0)))
        }
    }
}