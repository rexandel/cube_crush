package com.rexandel.cube_crush.viewmodel

import com.rexandel.cube_crush.model.GameModel
import com.rexandel.cube_crush.model.GameState
import android.util.Log

class GameStateManager(private val gameModel: GameModel) {

    fun getCurrentState(): GameState {
        return gameModel.getCurrentState()
    }

    fun placeShape(shapeIndex: Int, position: Pair<Int, Int>): PlaceShapeResult {
        val result = gameModel.placeShape(shapeIndex, position)

        return when (result) {
            is com.rexandel.cube_crush.model.PlaceShapeResult.Success -> {
                PlaceShapeResult.Success(result.linesCleared)
            }
            is com.rexandel.cube_crush.model.PlaceShapeResult.Failure -> {
                Log.e("GameStateManager", "Failed to place shape: ${result.message}. Shape: $shapeIndex, Position: $position")
                PlaceShapeResult.Failure(result.message)
            }
        }
    }

    fun canPlaceShape(shapeIndex: Int, position: Pair<Int, Int>): Boolean {
        val shape = getCurrentState().availableShapes.getOrNull(shapeIndex)
        return if (shape != null) {
            gameModel.canPlaceShape(shape, position)
        } else {
            false
        }
    }

    fun restartGame() {
        gameModel.restartGame()
    }

    fun updateHighScore(highScore: Int) {
        gameModel.updateHighScore(highScore)
    }
}