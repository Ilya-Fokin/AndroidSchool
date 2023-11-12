package com.example.lesson_5_fokin

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class FifthActivityContract : ActivityResultContract<Unit, String>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return FifthActivity.createStartIntent(context)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String {
        return if (resultCode == Activity.RESULT_OK) {
            intent?.getStringExtra(FifthActivity.KEY_RESULT).orEmpty()
        } else {
            ""
        }
    }
}