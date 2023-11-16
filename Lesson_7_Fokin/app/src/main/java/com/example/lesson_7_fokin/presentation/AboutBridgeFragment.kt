package com.example.lesson_7_fokin.presentation

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.lesson_7_fokin.R
import com.example.lesson_7_fokin.data.model.Bridge
import com.example.lesson_7_fokin.data.model.StatusBridge
import com.example.lesson_7_fokin.databinding.FragmentAboutBridgeBinding


class AboutBridgeFragment : Fragment(R.layout.fragment_about_bridge) {

    private val binding by viewBinding(FragmentAboutBridgeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bridge = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(BRIDGE_KEY, Bridge::class.java)
        } else {
            arguments?.getParcelable(BRIDGE_KEY)
        }

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
            var times = StringBuilder()
            repeat(this.size) {
                val startTimeStr = this[it].start as String
                times.append("$startTimeStr-")
                val endTimeStr = this[it].end as String
                times.append("$endTimeStr ")
            }
            binding.cardBridge.textViewTime.text = times
        }

        Glide.with(view)
            .load(choiceImg(bridge))
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

    private fun choiceImg(bridge: Bridge): String? {
        return when (bridge.status) {
            StatusBridge.SOON -> bridge.openUrl
            StatusBridge.REDUCED -> bridge.openUrl
            StatusBridge.DIVORCED -> bridge.closedUrl
        }
    }
}