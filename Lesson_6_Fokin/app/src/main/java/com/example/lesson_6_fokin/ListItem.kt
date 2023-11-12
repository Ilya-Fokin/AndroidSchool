package com.example.lesson_6_fokin

sealed class ListItem {
    data class ServiceItem(val item: Service) : ListItem()
}