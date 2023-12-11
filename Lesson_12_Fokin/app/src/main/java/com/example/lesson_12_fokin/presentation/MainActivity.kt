package com.example.lesson_12_fokin.presentation

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.example.lesson_12_fokin.R
import com.example.lesson_12_fokin.presentation.bridges.fragments.BridgesFragment

class MainActivity : AppCompatActivity(), NavigationController {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rootView = findViewById<View>(android.R.id.content)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                ResourcesCompat.getColor(resources, R.color.light_black, this.theme),
                ResourcesCompat.getColor(resources, R.color.light_black, this.theme)
            ),
            navigationBarStyle = SystemBarStyle.light(
                ResourcesCompat.getColor(resources, R.color.black, this.theme),
                ResourcesCompat.getColor(resources, R.color.black, this.theme)
            )
        )
        rootView.setOnApplyWindowInsetsListener { view, windowInsets ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val systemBarsInsets = windowInsets.getInsets(WindowInsets.Type.systemBars())
                view.updatePadding(
                    bottom = systemBarsInsets.bottom,
                    left = systemBarsInsets.left,
                    right = systemBarsInsets.right
                )
            } else {
                view.updatePadding(
                    bottom = windowInsets.systemWindowInsetBottom,
                    left = windowInsets.systemWindowInsetLeft,
                    right = windowInsets.systemWindowInsetRight
                )
            }
            windowInsets
        }

        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightNavigationBars = false
            isAppearanceLightStatusBars = false
        }
        //WindowCompat.setDecorFitsSystemWindows(window, false)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view_tag, BridgesFragment.newInstance())
            .commit()
    }

    override fun navigate(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view_tag, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun back() {
        supportFragmentManager.popBackStack()
    }
}