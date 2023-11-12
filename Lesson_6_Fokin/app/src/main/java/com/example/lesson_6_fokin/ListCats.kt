package com.example.lesson_6_fokin

sealed class ListCats {
    data class CatItem(val item: CatCard) : ListItem()
}