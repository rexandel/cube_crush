package com.rexandel.cube_crush.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.rexandel.cube_crush.model.Block
import com.rexandel.cube_crush.model.BlockColor
import com.rexandel.cube_crush.model.GameState
import kotlin.random.Random

class GameViewModel : ViewModel() {

    private val boardWidth = 8
    private val boardHeight = 8
    private val shapeColor = BlockColor.BLUE

    var gameState by mutableStateOf(createNewGame())
        private set

    private fun createNewGame(): GameState {
        val board = List(boardHeight) { y ->
            List(boardWidth) { x ->
                Block(x, y)
            }
        }

        val boardWithInitialBlocks = addInitialRandomBlocks(board)
        return GameState(
            board = boardWithInitialBlocks,
            score = 0,
            highScore = 0
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
                    newBoard[y][x] = newBoard[y][x].copy(color = shapeColor)
                    placed = true
                }
            }
        }
        return newBoard
    }

    private val squareShape = listOf(
        Pair(0, 0), Pair(1, 0),
        Pair(0, 1), Pair(1, 1)
    )

    fun placeSquare(position: Pair<Int, Int>): Boolean {
        val currentState = gameState

        if (canPlaceSquare(position)) {
            val newBoard = placeSquareOnBoard(position)
            val (boardAfterLineClear, linesCleared) = checkAndClearLines(newBoard)
            val newScore = currentState.score + (linesCleared * 100)

            gameState = currentState.copy(
                board = boardAfterLineClear,
                score = newScore,
                highScore = maxOf(currentState.highScore, newScore),
                isGameOver = checkGameOver(boardAfterLineClear)
            )
            return true
        }
        return false
    }

    private fun canPlaceSquare(position: Pair<Int, Int>): Boolean {
        val (startX, startY) = position

        for (block in squareShape) {
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

    private fun placeSquareOnBoard(position: Pair<Int, Int>): List<List<Block>> {
        val newBoard = gameState.board.map { it.toMutableList() }.toMutableList()
        val (startX, startY) = position

        for (block in squareShape) {
            val (dx, dy) = block
            val x = startX + dx
            val y = startY + dy

            newBoard[y][x] = newBoard[y][x].copy(color = shapeColor)
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

    private fun checkGameOver(board: List<List<Block>>): Boolean {
        for (y in 0 until boardHeight - 1) {
            for (x in 0 until boardWidth - 1) {
                if (canPlaceSquare(Pair(x, y))) {
                    return false
                }
            }
        }
        return true
    }

    fun restartGame() {
        gameState = createNewGame()
    }
}