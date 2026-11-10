
package com.example.tic_tac_toe.viewmodel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tic_tac_toe.view.GameViewModel

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = viewModel()
) {

    // Recoge los cambios del estado del tablero.
    val board by gameViewModel.board.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "TRES EN RAYA",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(25.dp))

        // Muestra el ganador, el empate o el turno actual.
        Text(
            text = when {
                board.winner != null ->
                    "¡Ha ganado ${board.winner}!"

                board.isDraw ->
                    "¡Empate!"

                else ->
                    "Turno del jugador: ${board.currentPlayer}"
            },
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(25.dp))

        // Muestra el tablero de 3x3.
        BoardGrid(
            cells = board.cells,
            onCellClick = { position ->
                gameViewModel.onCellClick(position)
            }
        )

        Spacer(modifier = Modifier.height(25.dp))

        // Botón para comenzar una partida nueva.
        Button(
            onClick = {
                gameViewModel.restartGame()
            }
        ) {
            Text(text = "Reiniciar partida")
        }
    }
}
