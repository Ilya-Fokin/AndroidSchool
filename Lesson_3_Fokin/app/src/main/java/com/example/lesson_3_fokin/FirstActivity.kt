package com.example.lesson_3_fokin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.lesson_3_fokin.databinding.FirstActivityBinding

class FirstActivity : AppCompatActivity() {
    private lateinit var binding: FirstActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FirstActivityBinding.inflate(layoutInflater) //initializing the binding class
        setContentView(binding.root)
        val profile: Profile = Profile("Карта №7898769", "Специалист","Анастасия", "Антонина", "any.box@gmail.ru",
            "HIE023UOI88", "Санкт-Петербург")
        loadProfile(profile)
        binding.backButton.setOnClickListener {
            super.finish()
        }
        binding.mainEditButton.setOnClickListener {
            Toast.makeText(this, getString(R.string.edit_toast), Toast.LENGTH_SHORT).show()
            true
        }
        binding.exitAccButton.setOnClickListener {
            Toast.makeText(this, getString(R.string.exit_acc_toast), Toast.LENGTH_SHORT).show()
            true
        }
        binding.viewPersonParamCity.editButton.setOnClickListener {
            Toast.makeText(this, getString(R.string.edit_city_toast), Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun loadProfile(profile: Profile) {
        with(binding) {
            numOfCard.text = profile.numOfCard
            post.text = profile.post

            viewPersonParamName.nameParam.text = getString(R.string.name)
            viewPersonParamName.param.text = profile.name

            viewPersonParamSurname.nameParam.text = getString(R.string.surname)
            viewPersonParamSurname.param.text = profile.surname

            viewPersonParamEmail.nameParam.text = getString(R.string.email)
            viewPersonParamEmail.param.text = profile.email

            viewPersonParamLogin.nameParam.text = getString(R.string.login)
            viewPersonParamLogin.param.text = profile.login

            viewPersonParamCity.nameParam.text = getString(R.string.city)
            viewPersonParamCity.param.text = profile.city
            viewPersonParamCity.editButton.visibility = View.VISIBLE
        }
    }
}
