package org.proven.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * CONTROLLER (MVC)
 * Conecta la vista (Compose) con el modelo (GameModel).
 */
class GameController {

    private val model = GameModel(2)

    var message by mutableStateOf("¡Bienvenido!")
        private set

    var buttonText by mutableStateOf("Start")
        private set

    var showButton by mutableStateOf(true)
        private set

    // Imagen de cada cubilete; su tamaño cambia al añadir cubiletes
    val cubes = mutableStateListOf<Int>().apply {
        repeat(2) { add(R.drawable.cubilete) }
    }

    fun onStart() {
        model.startNewRound()

        cubes.clear()
        repeat(model.totalCubes) { cubes.add(R.drawable.cubilete) }

        message = "Elige un cubilete (${model.totalCubes})"
        showButton = false
    }

    fun onCubeSelected(position: Int) {
        if (!model.isPlaying) return

        val result = model.checkSelection(position)

        if (result.isWin) {
            cubes[position - 1] = R.drawable.levantaok
            message = "¡Acertaste! Aciertos: ${model.score}"
            buttonText = "Siguiente (+1 cubilete)"
        } else {
            cubes[position - 1] = R.drawable.levantafail
            cubes[result.winningPosition - 1] = R.drawable.levantaok
            message = "¡Has perdido! Aciertos: ${model.score}"
            buttonText = "Start"
        }
        showButton = true
    }
}