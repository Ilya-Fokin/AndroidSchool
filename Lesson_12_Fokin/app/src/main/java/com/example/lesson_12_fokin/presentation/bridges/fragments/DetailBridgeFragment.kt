package com.example.lesson_12_fokin.presentation.bridges.fragments

import android.media.Image
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.lesson_12_fokin.R
import com.example.lesson_12_fokin.data.remote.LoadState
import com.example.lesson_12_fokin.data.remote.model.BridgeMapper
import com.example.lesson_12_fokin.data.remote.model.Divorce
import com.example.lesson_12_fokin.databinding.FragmentBridgeDetailBinding
import com.example.lesson_12_fokin.presentation.NavigationController
import com.example.lesson_12_fokin.presentation.bridges.Bridge
import com.example.lesson_12_fokin.presentation.bridges.viewModels.DetailBridgeViewModel

class DetailBridgeFragment : BaseFragment(R.layout.fragment_bridge_detail) {

    private val binding by viewBinding(FragmentBridgeDetailBinding::bind)
    private val viewModel: DetailBridgeViewModel by appViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val rootView = inflater.inflate(R.layout.fragment_bridge_detail, container, false)
        val imageViewAppBar = rootView.findViewById<ImageView>(R.id.imageViewBackgroundAppBar)
        val collapsingToolbar = rootView.findViewById<View>(R.id.collapsingToolbar)
        val toolbar = rootView.findViewById<View>(R.id.toolbar)

        collapsingToolbar.setOnApplyWindowInsetsListener { view, windowInsets ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val systemBarsInsets = windowInsets.getInsets(WindowInsets.Type.systemBars())
                imageViewAppBar.updateLayoutParams {
                    height += systemBarsInsets.top
                }
                toolbar.updatePadding(
                    top = systemBarsInsets.top
                )
            } else {
                toolbar.updatePadding(
                    top = windowInsets.systemWindowInsetTop
                )
                imageViewAppBar.updateLayoutParams {
                    height += windowInsets.systemWindowInsetTop
                }
            }
            windowInsets
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt(BRIDGE_KEY)
        if (id != null) {
            viewModel.loadBridgeById(id)
        }
        val times = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArrayList(BRIDGE_TIME, Divorce::class.java)
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

    private fun detailBridgeLiveDataObserve(view: View, divorces: List<Divorce>?) {
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

    companion object {
        private const val BRIDGE_KEY = "bridge_key"
        private const val BRIDGE_TIME = "bridge_time"

        fun newInstance(id: Int, divorces: ArrayList<Divorce>? = null): DetailBridgeFragment {
            return DetailBridgeFragment().also {
                it.arguments = Bundle().apply {
                    putInt(BRIDGE_KEY, id)
                    putParcelableArrayList(BRIDGE_TIME, divorces)
                }
            }
        }
    }
}