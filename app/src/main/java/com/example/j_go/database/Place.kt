package com.example.j_go.database

data class Place(
    val place_id: Int,
    val place_name: String,
    val category: String,
    val price: Int,
    val latitude: Double,
    val longitude: Double,
    val description_indonesia: String,
    val description_english: String,
    val image_res: String,
    val place_rate: Double
)

