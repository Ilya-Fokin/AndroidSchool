package com.example.lesson_5_fokin

sealed class ListItem {
    data class Item(val itemCard: ItemCard) : ListItem()
}