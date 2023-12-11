package com.example.lesson_12_fokin.presentation.bridges.fragments

import androidx.annotation.LayoutRes
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModel
import com.example.lesson_12_fokin.di.applicationComponent.DaggerViewModelFactory
import com.example.lesson_12_fokin.presentation.BoundingBoxBuilder
import com.yandex.mapkit.map.ClusterTapListener
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment(@LayoutRes layoutRes: Int) : DaggerFragment(layoutRes) {

    @Inject
    lateinit var daggerViewModelFactory: DaggerViewModelFactory
    protected inline fun <reified VM : ViewModel> appViewModels() =
        createViewModelLazy(VM::class, { viewModelStore }) {
            daggerViewModelFactory
        }
}