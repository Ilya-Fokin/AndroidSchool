package com.example.lesson_3_fokin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lesson_3_fokin.databinding.ThirdActivityBinding

class ThirdActivity : AppCompatActivity() {
    private lateinit var binding: ThirdActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ThirdActivityBinding.inflate(layoutInflater) //initializing the binding class
        setContentView(binding.root)
        val profile: Profile = Profile(
            7898769, "Специалист", "Анастасия", "Антонина", "any.box@gmail.ru",
            "HIE023UOI88", "Санкт-Петербург"
        )
        loadProfile(profile)
        binding.toolbar.setNavigationOnClickListener {
            super.finish()
        }
        binding.toolbar.setOnMenuItemClickListener {
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
            numOfCard.text = getString(R.string.num_of_card, profile.numOfCard);
            post.text = profile.post

            viewPersonParamName.nameParam.text = getString(com.example.lesson_3_fokin.R.string.name)
            viewPersonParamName.param.text = profile.name

            viewPersonParamSurname.nameParam.text = getString(com.example.lesson_3_fokin.R.string.surname)
            viewPersonParamSurname.param.text = profile.surname

            viewPersonParamEmail.nameParam.text = getString(com.example.lesson_3_fokin.R.string.email)
            viewPersonParamEmail.param.text = profile.email

            viewPersonParamLogin.nameParam.text = getString(com.example.lesson_3_fokin.R.string.login)
            viewPersonParamLogin.param.text = profile.login

            viewPersonParamCity.nameParam.text = getString(com.example.lesson_3_fokin.R.string.city)
            viewPersonParamCity.param.text = profile.city
            viewPersonParamCity.editButton.visibility = android.view.View.VISIBLE
        }
    }

}