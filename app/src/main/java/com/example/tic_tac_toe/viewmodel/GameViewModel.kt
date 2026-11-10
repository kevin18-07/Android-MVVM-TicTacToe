
package com.example.tic_tac_toe.view

import androidx.lifecycle.ViewModel
import com.example.tic_tac_toe.model.BoardModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {

    // Estado privado que podemos modificar.
    private val _board = MutableStateFlow(
        BoardModel.createInitialBoard()
    )

    // Estado público que solo permite consultar los datos.
    val board = _board.asStateFlow()

    // Se ejecuta cuando el usuario pulsa una casilla.
    fun onCellClick(position: Int) {
        _board.value = _board.value.makeMove(position)
    }

    // Reinicia la partida creando un tablero nuevo.
    fun restartGame() {
        _board.value = BoardModel.createInitialBoard()
    }
}
