package com.example.lesson_0_fokin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val player: Player = Player(5,6,25, arrayOf(5,5,6))
        findViewById<TextView>(R.id.textViewDescription).text = player.toString()
    }
}