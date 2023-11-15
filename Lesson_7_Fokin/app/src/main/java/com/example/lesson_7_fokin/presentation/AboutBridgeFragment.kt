package com.example.lesson_7_fokin.presentation

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.lesson_7_fokin.R
import com.example.lesson_7_fokin.data.model.Bridge
import com.example.lesson_7_fokin.data.model.StatusBridge
import com.example.lesson_7_fokin.databinding.FragmentAboutBridgeBinding

class AboutBridgeFragment : Fragment(R.layout.fragment_about_bridge) {

    private val binding by viewBinding(FragmentAboutBridgeBinding::bind)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bridge = arguments?.getParcelable(BRIDGE_KEY, Bridge::class.java)
        binding.cardBridge.textViewNameBridge.text = bridge?.name

        when (bridge?.status) {
            StatusBridge.SOON -> binding.cardBridge.imageViewBridge.setImageResource(R.drawable.ic_brige_soon)
            StatusBridge.DIVORCED -> binding.cardBridge.imageViewBridge.setImageResource(R.drawable.ic_brige_late)
            StatusBridge.REDUCED -> binding.cardBridge.imageViewBridge.setImageResource(R.drawable.ic_brige_normal)
            else -> {
                error("Unknown img")
            }
        }

        bridge.divorces?.apply {
            binding.cardBridge.textViewTime1.text = resources.getString(R.string.divorce, this[0].start, this[0].end)
            if (this.size > 1) {
                binding.cardBridge.textViewTime2.text = resources.getString(R.string.divorce, this[1].start, this[1].end)
            }
        }

        Glide.with(view)
            .load(bridge.openUrl)
            .into(binding.imageViewBackgroundAppBar)

        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.textViewDescription.text = bridge.description
    }

    companion object {
        private const val BRIDGE_KEY = "bridge_key"

        fun newInstance(bridge: Bridge): AboutBridgeFragment {
            return AboutBridgeFragment().also {
                it.arguments = Bundle().apply {
                    putParcelable(BRIDGE_KEY, bridge)
                }
            }
        }
    }
}