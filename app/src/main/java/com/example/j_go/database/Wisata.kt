package com.example.j_go.database

data class Wisata(
    val name: String,
    val category: String,
    val ticket_price: Int,
    val latitude: Double,
    val longitude: Double,
    val description: String,
    val image_res: String,
    val rate: Double
)

