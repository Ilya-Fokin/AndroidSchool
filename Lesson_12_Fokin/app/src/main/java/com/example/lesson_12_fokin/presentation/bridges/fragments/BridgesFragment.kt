package com.example.lesson_12_fokin.presentation.bridges.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_12_fokin.R
import com.example.lesson_12_fokin.data.remote.LoadState
import com.example.lesson_12_fokin.databinding.FragmentBridgesBinding
import com.example.lesson_12_fokin.presentation.NavigationController
import com.example.lesson_12_fokin.presentation.bridges.BridgeCardListener
import com.example.lesson_12_fokin.presentation.bridges.BridgesAdapter
import com.example.lesson_12_fokin.presentation.bridges.viewModels.BridgesViewModel

class BridgesFragment : BaseFragment(R.layout.fragment_bridges) {

    private val binding by viewBinding(FragmentBridgesBinding::bind)

    private val viewModel: BridgesViewModel by appViewModels()

    private val bridgesAdapter = BridgesAdapter()

    private var activityNav: NavigationController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadBridges()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInsets()

        activityNav = activity as NavigationController

        bridgeLiveDataObserve()

        binding.recyclerView.adapter = bridgesAdapter.apply {
            bridgeCardListener = BridgeCardListener {
                val detailFragment = DetailBridgeFragment.newInstance(it.id, ArrayList(it.divorces))
                activityNav?.navigate(detailFragment)
            }
        }
        binding.materialToolbar.menu.findItem(R.id.menuButtonToMap).setOnMenuItemClickListener {
            val mapFragment = MapBridgesFragment.newInstance()
            activityNav?.navigateWithoutBack(mapFragment)
            true
        }
        binding.buttonUpdate.setOnClickListener {
            viewModel.loadBridges()
        }
    }

    override fun onDestroyView() {
        activityNav = null
        super.onDestroyView()
    }

    private fun bridgeLiveDataObserve() {
        viewModel.bridgesLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoadState.Loading -> {
                    binding.errorLayout.isVisible = false
                    binding.recyclerView.isVisible = false
                    binding.progressBar.isVisible = true
                }

                is LoadState.Data -> {
                    bridgesAdapter.submitList(state.data)
                    binding.errorLayout.isVisible = false
                    binding.progressBar.isVisible = false
                    binding.recyclerView.isVisible = true
                }

                is LoadState.Error -> {
                    binding.progressBar.isVisible = false
                    binding.recyclerView.isVisible = false
                    binding.errorMessage.text = state.exception.message
                    binding.errorLayout.isVisible = true
                }
            }
        }
    }

    private fun initInsets() {
        binding.materialToolbar.setOnApplyWindowInsetsListener { view, windowInsets ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val systemBarsInsets = windowInsets.getInsets(WindowInsets.Type.systemBars())
                view.updatePadding(
                    top = systemBarsInsets.top
                )
            } else {
                view.updatePadding(
                    top = windowInsets.systemWindowInsetTop
                )
            }
            windowInsets
        }
    }

    companion object {

        fun newInstance(): BridgesFragment {
            return BridgesFragment()
        }
    }
}