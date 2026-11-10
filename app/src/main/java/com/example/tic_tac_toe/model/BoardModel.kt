package com.example.tic_tac_toe.model

data class BoardModel(
    val cells: List<CellModel>,
    val currentPlayer: String = "X",
    val winner: String? = null,
    val isDraw: Boolean = false,
    val isGameOver: Boolean = false
) {

    companion object {

        // Crea un tablero inicial con 9 casillas vacías.
        fun createInitialBoard(): BoardModel {
            val cells = (0..8).map { position ->
                CellModel(
                    id = position,
                    position = position
                )
            }

            return BoardModel(cells = cells)
        }
    }

    // Realiza una jugada en la posición seleccionada.
    fun makeMove(position: Int): BoardModel {

        // No permite jugar si la partida ha terminado.
        if (isGameOver) return this

        // Comprueba que la posición sea válida.
        if (position !in cells.indices) return this

        // No permite ocupar una casilla que ya tiene símbolo.
        if (cells[position].symbol != null) return this

        // Crea una nueva lista con la casilla actualizada.
        val updatedCells = cells.map { cell ->
            if (cell.position == position) {
                cell.copy(symbol = currentPlayer)
            } else {
                cell
            }
        }

        // Comprueba si alguien ha ganado o hay empate.
        return copy(cells = updatedCells).checkWinner()
    }

    // Comprueba las combinaciones ganadoras.
    fun checkWinner(): BoardModel {

        val combinations = listOf(
            listOf(0, 1, 2),
            listOf(3, 4, 5),
            listOf(6, 7, 8),
            listOf(0, 3, 6),
            listOf(1, 4, 7),
            listOf(2, 5, 8),
            listOf(0, 4, 8),
            listOf(2, 4, 6)
        )

        // Comprueba todas las combinaciones posibles.
        for (combination in combinations) {

            val (a, b, c) = combination

            val symbol = cells[a].symbol

            if (symbol != null &&
                symbol == cells[b].symbol &&
                symbol == cells[c].symbol
            ) {
                return copy(
                    winner = symbol,
                    isGameOver = true
                )
            }
        }

        // Si todas las casillas están ocupadas, hay empate.
        if (cells.all { it.symbol != null }) {
            return copy(
                isDraw = true,
                isGameOver = true
            )
        }

        // Cambia el turno del jugador.
        val nextPlayer = if (currentPlayer == "X") "O" else "X"

        return copy(currentPlayer = nextPlayer)
    }
}