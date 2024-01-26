package com.example.lesson_12_fokin.presentation.bridges.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.lesson_12_fokin.R
import com.example.lesson_12_fokin.data.remote.LoadState
import com.example.lesson_12_fokin.data.remote.model.ApiDivorce
import com.example.lesson_12_fokin.data.remote.model.BridgeMapper
import com.example.lesson_12_fokin.databinding.FragmentBridgeDetailBinding
import com.example.lesson_12_fokin.presentation.NavigationController
import com.example.lesson_12_fokin.data.remote.model.Bridge
import com.example.lesson_12_fokin.presentation.bridges.viewModels.DetailBridgeViewModel

class DetailBridgeFragment : BaseFragment(R.layout.fragment_bridge_detail) {

    private val binding by viewBinding(FragmentBridgeDetailBinding::bind)

    private val viewModel: DetailBridgeViewModel by appViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInsets()

        val id = arguments?.getInt(BRIDGE_KEY)
        if (id != null) {
            viewModel.loadBridgeById(id)
        }
        val times = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArrayList(BRIDGE_TIME, ApiDivorce::class.java)
        } else {
            arguments?.getParcelableArrayList(BRIDGE_TIME)
        }
        detailBridgeLiveDataObserve(view, times)
        binding.toolbar.setNavigationOnClickListener {
            (activity as? NavigationController)?.back()
        }
        binding.buttonUpdate.setOnClickListener {
            if (id != null) {
                viewModel.loadBridgeById(id)
            }
        }
    }

    private fun detailBridgeLiveDataObserve(view: View, divorces: List<ApiDivorce>?) {
        viewModel.bridgeLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoadState.Loading -> {
                    binding.errorLayout.isVisible = false
                    binding.appBar.isVisible = false
                    binding.nestedScrollView.isVisible = false
                    binding.progressBar.isVisible = true
                }

                is LoadState.Data -> {
                    val bridge: Bridge = if (divorces == null) {
                        BridgeMapper.toBridge(state.data)
                    } else {
                        BridgeMapper.toBridgeWithoutDivorces(state.data, divorces)
                    }
                    binding.bridgeRowView.bind(bridge)
                    Glide.with(view)
                        .asBitmap()
                        .load(bridge.currentImgUrl)
                        .into(binding.imageViewBackgroundAppBar)
                    binding.textViewDescription.text = bridge.description

                    binding.errorLayout.isVisible = false
                    binding.progressBar.isVisible = false
                    binding.appBar.isVisible = true
                    binding.nestedScrollView.isVisible = true
                }

                is LoadState.Error -> {
                    binding.appBar.isVisible = false
                    binding.progressBar.isVisible = false
                    binding.nestedScrollView.isVisible = false
                    binding.errorMessage.text = state.toString()
                    binding.errorLayout.isVisible = true
                }
            }
        }
    }

    private fun initInsets() {
        binding.collapsingToolbar.setOnApplyWindowInsetsListener { view, windowInsets ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val systemBarsInsets = windowInsets.getInsets(WindowInsets.Type.systemBars())
                binding.imageViewBackgroundAppBar.updateLayoutParams {
                    height += systemBarsInsets.top
                }
                binding.toolbar.updatePadding(
                    top = systemBarsInsets.top
                )
            } else {
                binding.toolbar.updatePadding(
                    top = windowInsets.systemWindowInsetTop
                )
                binding.imageViewBackgroundAppBar.updateLayoutParams {
                    height += windowInsets.systemWindowInsetTop
                }
            }
            windowInsets
        }
    }


    companion object {
        private const val BRIDGE_KEY = "bridge_key"
        private const val BRIDGE_TIME = "bridge_time"

        fun newInstance(id: Int, divorces: ArrayList<ApiDivorce>? = null): DetailBridgeFragment {
            return DetailBridgeFragment().also {
                it.arguments = Bundle().apply {
                    putInt(BRIDGE_KEY, id)
                    putParcelableArrayList(BRIDGE_TIME, divorces)
                }
            }
        }
    }
}