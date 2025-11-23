package com.rexandel.cube_crush.domain.entities

data class GameState(
    val board: List<List<Block>>,
    val availableShapes: List<Shape>,
    val score: Int = 0,
    val highScore: Int = 0,
    val isGameOver: Boolean = false
)

sealed class PlaceShapeResult {
    data class Success(val linesCleared: Int) : PlaceShapeResult()
    data class Failure(val message: String) : PlaceShapeResult()
}