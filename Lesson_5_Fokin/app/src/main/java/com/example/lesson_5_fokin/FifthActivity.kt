package com.example.lesson_5_fokin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_5_fokin.databinding.FifthActivityBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class FifthActivity : AppCompatActivity() {

    private val binding by viewBinding(FifthActivityBinding::bind)

    private val data by lazy { intent.getParcelableExtra<Data>("key_data") }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fifth_activity)

        binding.buttonOk.setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(KEY_RESULT, binding.editTextQuery.text?.toString().orEmpty())
            })
            finish()
        }

        binding.buttonSave.setOnClickListener {
            val input = binding.editTextQuery.text.toString()
            val data = Data(input)
            intent = Intent(this, FifthActivity::class.java)
            intent.putExtra("key_data", data)
            startActivity(intent)
        }
    }

    companion object {
        const val KEY_RESULT = "key_result"

        fun createStartIntent(context: Context): Intent {
            return Intent(context, FifthActivity::class.java)
        }
    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            binding.textViewData.text = data?.value
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("key_data", data)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.textViewData.text = savedInstanceState.getParcelable<Data>("key_data")?.value
    }
}