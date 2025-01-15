package com.example.arcanavault.model.data

interface IItem {
    val index: String
    val name: String
    val level: Int?
    val url: String
    val imageUrl: String
    var isFavorite: Boolean
}
