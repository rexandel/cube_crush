package com.rexandel.cube_crush.model

data class Block(
    val x: Int,
    val y: Int,
    val color: BlockColor? = null
)

enum class BlockColor(val colorValue: androidx.compose.ui.graphics.Color) {
    BLUE(androidx.compose.ui.graphics.Color(0xFF2196F3))
}

data class Shape(
    val type: ShapeType,
    val color: BlockColor,
    val blocks: List<Pair<Int, Int>>
)

enum class ShapeType {
    SQUARE,
}
data class GameState(
    val board: List<List<Block>>,
    val availableShapes: List<Shape?>,
    val score: Int = 0,
    val highScore: Int = 0,
    val isGameOver: Boolean = false
)