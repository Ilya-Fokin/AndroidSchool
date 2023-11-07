package com.example.lesson_5_fokin

import android.net.Uri

data class ItemCard(
    val title: String,
    val companyName: String,
    val address: String,
    val imgUri: Uri)