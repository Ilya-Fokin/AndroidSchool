package com.example.lesson_8_fokin.presentation

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class StaggeredGridItemDecoration(private val space: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.left = space
        outRect.right = space
        outRect.bottom = space

        if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) == 1) {
            outRect.top = space
        } else {
            outRect.top = 0
        }
        if (parent.getChildLayoutPosition(view) % 2 != 1 || parent.getChildLayoutPosition(view) == 0) {
            outRect.right = 0
        } else {
            outRect.right = space
        }
    }
}