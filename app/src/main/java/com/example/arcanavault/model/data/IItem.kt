package com.example.arcanavault.model.data

interface IItem {
    val index: String
    val name: String
    val level: Int?
    val url: String
    val isFavorite: Boolean?
    val description: List<String>
    val shortDescription: String
}