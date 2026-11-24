package org.proven.app

import kotlin.random.Random

data class GameResult(val isWin: Boolean, val winningPosition: Int)

/**
 * MODEL (MVC)
 * - Empieza con 2 cubiletes.
 * - Si el jugador acierta -> en la siguiente ronda se añade un cubilete.
 * - Si falla -> fin de partida; el siguiente Start vuelve a empezar con 2.
 */
class GameModel(private val initialCubes: Int = 2) {

    var totalCubes: Int = initialCubes
        private set

    var isPlaying: Boolean = false
        private set

    var positionBall: Int = 0
        private set

    // Número de aciertos seguidos de la partida actual
    var score: Int = 0
        private set

    // Indica que la última ronda se falló (partida terminada)
    var isGameOver: Boolean = false
        private set

    private var pendingAddCube = false

    fun startNewRound() {
        if (isGameOver) {
            // Nueva partida: se reinicia todo
            totalCubes = initialCubes
            score = 0
            isGameOver = false
            pendingAddCube = false
        } else if (pendingAddCube) {
            // Acertó la ronda anterior: añadimos un cubilete
            totalCubes++
            pendingAddCube = false
        }

        isPlaying = true
        positionBall = Random.nextInt(1, totalCubes + 1)
    }

    fun checkSelection(selectedCube: Int): GameResult {
        if (!isPlaying) return GameResult(false, positionBall)

        isPlaying = false
        val isWin = (selectedCube == positionBall)

        if (isWin) {
            score++
            pendingAddCube = true
        } else {
            isGameOver = true
        }

        return GameResult(isWin, positionBall)
    }
}