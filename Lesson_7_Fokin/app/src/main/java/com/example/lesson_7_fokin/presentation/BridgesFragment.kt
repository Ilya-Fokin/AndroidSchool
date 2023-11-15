package com.example.lesson_7_fokin.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_7_fokin.R
import com.example.lesson_7_fokin.data.ApiClient
import com.example.lesson_7_fokin.data.model.BridgeCardListener
import com.example.lesson_7_fokin.databinding.FragmentBridgesBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BridgesFragment : Fragment(R.layout.fragment_bridges) {
    private val binding by viewBinding(FragmentBridgesBinding::bind)
    private val bridgesAdapter = BridgesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            binding.progressBar.isVisible = true
            delay(1000)
            try {
                val bridges = ApiClient.apiService.getBridges()

                if (bridges.isEmpty()) {
                    binding.progressBar.isVisible = false
                    binding.errorMessage.text = "Тут пусто"
                    binding.errorLayout.isVisible = true
                } else bridgesAdapter.setBridges(bridges)
            } catch (e: Exception) {
                binding.errorMessage.text = e.message
                binding.errorLayout.isVisible = true
            } finally {
                binding.progressBar.isVisible = false
            }
        }

        binding.recyclerView.adapter = bridgesAdapter.apply {
            bridgeCardListener = BridgeCardListener {
                val fragment = AboutBridgeFragment.newInstance(it)
                (activity as? NavigationController)?.navigate(fragment)
            }
        }

        binding.buttonUpdate.setOnClickListener {
            (activity as? NavigationController)?.navigate(newInstance())
        }
    }

    companion object {
        fun newInstance(): BridgesFragment {
            return BridgesFragment()
        }
    }
}