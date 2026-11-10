package com.example.tic_tac_toe.model

// Representa una casilla del tablero.
data class CellModel(
    val id: Int,
    val position: Int,
    val symbol: String? = null
)