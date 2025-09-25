package com.example.cyberdream.data

data class PuntoInteres(
    val id: String,
    val nombre: String,
    val descripcion: String
)

data class Edificio(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val puntos: List<PuntoInteres>
)

data class Barrio(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val edificios: List<Edificio>
)
