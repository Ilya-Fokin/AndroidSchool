package com.example.lesson_2_fokin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.buttonActivityText).setOnClickListener {
            val intent = Intent(this, ActivityWithText::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.buttonActivityDrawable).setOnClickListener {
            val intent = Intent(this, ActivityWithDrawable::class.java)
            startActivity(intent)
        }
    }
}