package com.rexandel.cube_crush.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.rexandel.cube_crush.model.*
import kotlin.random.Random

class GameViewModel : ViewModel() {

    private val boardWidth = 8
    private val boardHeight = 8
    private val shapesPerMove = 3
    var gameState by mutableStateOf(createNewGame())
        private set

    private fun createNewGame(): GameState {
        val board = List(boardHeight) { y ->
            List(boardWidth) { x ->
                Block(x, y)
            }
        }

        val boardWithInitialBlocks = addInitialRandomBlocks(board)
        val initialShapes = generateRandomShapes(shapesPerMove)

        return GameState(
            board = boardWithInitialBlocks,
            availableShapes = initialShapes,
            score = 0,
            highScore = 0,
            isGameOver = false
        )
    }

    private fun addInitialRandomBlocks(board: List<List<Block>>): List<List<Block>> {
        val newBoard = board.map { it.toMutableList() }.toMutableList()

        val initialBlockCount = Random.nextInt(10, 16)
        repeat(initialBlockCount) {
            var placed = false
            while (!placed) {
                val x = Random.nextInt(boardWidth)
                val y = Random.nextInt(boardHeight)
                if (newBoard[y][x].color == null) {
                    val randomColor = BlockColor.values().random()
                    newBoard[y][x] = newBoard[y][x].copy(color = randomColor)
                    placed = true
                }
            }
        }
        return newBoard
    }

    private fun generateRandomShapes(count: Int): List<Shape> {
        return List(count) {
            createSquareShape()
        }
    }

    private fun createSquareShape(): Shape {
        val color = BlockColor.values().random()
        return Shape(
            type = ShapeType.SQUARE,
            color = color,
            blocks = listOf(
                Pair(0, 0), Pair(1, 0),
                Pair(0, 1), Pair(1, 1)
            )
        )
    }

    fun placeShape(shapeIndex: Int, position: Pair<Int, Int>): Boolean {
        val currentState = gameState

        if (shapeIndex !in currentState.availableShapes.indices) {
            return false
        }

        val shape = currentState.availableShapes[shapeIndex]

        if (shape == null) {
            return false
        }

        if (canPlaceShape(shape, position)) {
            val newBoard = placeShapeOnBoard(shape, position)
            val (boardAfterLineClear, linesCleared) = checkAndClearLines(newBoard)
            val newScore = currentState.score + (linesCleared * 100)

            val newShapes = currentState.availableShapes.toMutableList().apply {
                this[shapeIndex] = null
            }

            val allShapesUsed = newShapes.all { it == null }

            gameState = currentState.copy(
                board = boardAfterLineClear,
                availableShapes = newShapes,
                score = newScore,
                highScore = maxOf(currentState.highScore, newScore),
                isGameOver = checkGameOver(boardAfterLineClear, newShapes)
            )

            if (allShapesUsed) {
                generateNewShapes()
            }
            return true
        }
        return false
    }

    private fun generateNewShapes() {
        val currentState = gameState
        val newShapes = generateRandomShapes(shapesPerMove)

        gameState = currentState.copy(
            availableShapes = newShapes,
            isGameOver = checkGameOver(currentState.board, newShapes)
        )
    }

    private fun canPlaceShape(shape: Shape, position: Pair<Int, Int>): Boolean {
        val (startX, startY) = position

        for (block in shape.blocks) {
            val (dx, dy) = block
            val x = startX + dx
            val y = startY + dy

            if (x < 0 || x >= boardWidth || y < 0 || y >= boardHeight) {
                return false
            }

            if (gameState.board[y][x].color != null) {
                return false
            }
        }
        return true
    }

    private fun placeShapeOnBoard(shape: Shape, position: Pair<Int, Int>): List<List<Block>> {
        val newBoard = gameState.board.map { it.toMutableList() }.toMutableList()
        val (startX, startY) = position

        for (block in shape.blocks) {
            val (dx, dy) = block
            val x = startX + dx
            val y = startY + dy

            newBoard[y][x] = newBoard[y][x].copy(color = shape.color)
        }
        return newBoard
    }

    private fun checkAndClearLines(board: List<List<Block>>): Pair<List<List<Block>>, Int> {
        var linesCleared = 0
        var newBoard = board

        for (y in 0 until boardHeight) {
            if (isLineFull(board[y])) {
                newBoard = clearLine(newBoard, y)
                linesCleared++
            }
        }

        for (x in 0 until boardWidth) {
            val column = List(boardHeight) { y -> board[y][x] }
            if (isLineFull(column)) {
                newBoard = clearColumn(newBoard, x)
                linesCleared++
            }
        }

        return Pair(newBoard, linesCleared)
    }

    private fun isLineFull(line: List<Block>): Boolean {
        return line.all { it.color != null }
    }

    private fun clearLine(board: List<List<Block>>, lineIndex: Int): List<List<Block>> {
        val newBoard = board.map { it.toMutableList() }.toMutableList()
        for (x in 0 until boardWidth) {
            newBoard[lineIndex][x] = newBoard[lineIndex][x].copy(color = null)
        }
        return newBoard
    }

    private fun clearColumn(board: List<List<Block>>, columnIndex: Int): List<List<Block>> {
        val newBoard = board.map { it.toMutableList() }.toMutableList()
        for (y in 0 until boardHeight) {
            newBoard[y][columnIndex] = newBoard[y][columnIndex].copy(color = null)
        }
        return newBoard
    }

    private fun checkGameOver(board: List<List<Block>>, availableShapes: List<Shape?>): Boolean {
        for (shape in availableShapes) {
            if (shape == null) continue

            for (y in 0 until boardHeight - 1) {
                for (x in 0 until boardWidth - 1) {
                    if (canPlaceShape(shape, Pair(x, y))) {
                        return false
                    }
                }
            }
        }
        return true
    }

    fun restartGame() {
        gameState = createNewGame()
    }
}