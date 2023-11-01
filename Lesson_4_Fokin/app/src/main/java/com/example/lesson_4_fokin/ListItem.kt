package com.example.lesson_4_fokin

sealed class ListItem {
    data class DetailItem(val detailCard: DetailCard) : ListItem()
    data class BaseItem(val baseCard: BaseCard) : ListItem()
}
