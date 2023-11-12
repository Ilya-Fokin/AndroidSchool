package com.example.lesson_6_fokin.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_6_fokin.R
import com.example.lesson_6_fokin.databinding.FirstFragmentBinding

class FirstFragment : Fragment(R.layout.first_fragment) {

    private val binding by viewBinding(FirstFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.materialToolbar.menu.findItem(R.id.menuButtonSearch).setOnMenuItemClickListener {
            Toast.makeText(binding.root.context, "Тык на лупу", Toast.LENGTH_SHORT).show()
            true
        }

        binding.materialToolbar.menu.findItem(R.id.menuButtonItem1).setOnMenuItemClickListener {
            Toast.makeText(binding.root.context, "Тык на item1", Toast.LENGTH_SHORT).show()
            true
        }

        binding.materialToolbar.menu.findItem(R.id.menuButtonItem2).setOnMenuItemClickListener {
            Toast.makeText(binding.root.context, "Тык на item2", Toast.LENGTH_SHORT).show()
            true
        }

        binding.materialToolbar.menu.findItem(R.id.menuButtonItem3).setOnMenuItemClickListener {
            Toast.makeText(binding.root.context, "Тык на item3", Toast.LENGTH_SHORT).show()
            true
        }

        binding.materialToolbar.menu.findItem(R.id.menuButtonItem4).setOnMenuItemClickListener {
            Toast.makeText(binding.root.context, "Тык на item4", Toast.LENGTH_SHORT).show()
            true
        }
    }
}