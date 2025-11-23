package com.rexandel.cube_crush.domain.game

import com.rexandel.cube_crush.domain.entities.Block
import com.rexandel.cube_crush.domain.entities.BlockColor
import com.rexandel.cube_crush.domain.entities.Shape
import kotlin.random.Random

class BoardManager(
    private val boardWidth: Int = 8,
    private val boardHeight: Int = 8
) {
    fun createEmptyBoard(): List<List<Block>> {
        return List(boardHeight) { y ->
            List(boardWidth) { x ->
                Block(x, y)
            }
        }
    }

    fun addInitialRandomBlocks(board: List<List<Block>>): List<List<Block>> {
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

    fun placeShapeOnBoard(
        board: List<List<Block>>,
        shape: Shape,
        position: Pair<Int, Int>
    ): List<List<Block>> {
        val newBoard = board.map { it.toMutableList() }.toMutableList()
        val (startX, startY) = position

        for (y in 0 until shape.height) {
            for (x in 0 until shape.width) {
                if (shape.matrix[y][x]) {
                    val boardX = startX + x
                    val boardY = startY + y

                    if (boardX in 0 until boardWidth && boardY in 0 until boardHeight) {
                        newBoard[boardY][boardX] = newBoard[boardY][boardX].copy(color = shape.color)
                    }
                }
            }
        }
        return newBoard
    }

    fun checkAndClearLines(board: List<List<Block>>): Pair<List<List<Block>>, Int> {
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
}