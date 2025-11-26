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

    fun addInitialRandomBlocks(
        board: List<List<Block>>,
        availableShapes: List<Shape>
    ): List<List<Block>> {
        val newBoard = board.map { it.toMutableList() }.toMutableList()

        val guaranteedPlacementBoard = createGuaranteedPlacementBoard(newBoard, availableShapes)

        return addAdditionalRandomBlocks(guaranteedPlacementBoard, availableShapes)
    }

    private fun createGuaranteedPlacementBoard(
        board: List<List<Block>>,
        availableShapes: List<Shape>
    ): List<List<Block>> {
        val newBoard = board.map { it.toMutableList() }.toMutableList()
        val shapePlacementPositions = mutableSetOf<Pair<Int, Int>>()

        availableShapes.forEach { shape ->
            val possiblePositions = findPossiblePlacementPositions(newBoard, shape)
            if (possiblePositions.isNotEmpty()) {
                val position = possiblePositions.random()
                shapePlacementPositions.addAll(getShapeCoverage(shape, position))
            }
        }

        val totalCells = boardWidth * boardHeight
        val maxInitialBlocks = totalCells / 4

        var placedBlocks = 0
        while (placedBlocks < maxInitialBlocks) {
            val x = Random.nextInt(boardWidth)
            val y = Random.nextInt(boardHeight)
            val position = Pair(x, y)

            if (!shapePlacementPositions.contains(position) && newBoard[y][x].color == null) {
                val randomColor = BlockColor.values().random()
                newBoard[y][x] = newBoard[y][x].copy(color = randomColor)
                placedBlocks++

                if (!canAllShapesBePlaced(newBoard, availableShapes)) {
                    newBoard[y][x] = newBoard[y][x].copy(color = null)
                    placedBlocks--
                    break
                }
            }
        }

        return newBoard
    }

    private fun addAdditionalRandomBlocks(
        board: List<List<Block>>,
        availableShapes: List<Shape>
    ): List<List<Block>> {
        val newBoard = board.map { it.toMutableList() }.toMutableList()
        val emptyPositions = mutableListOf<Pair<Int, Int>>()

        for (y in 0 until boardHeight) {
            for (x in 0 until boardWidth) {
                if (newBoard[y][x].color == null) {
                    emptyPositions.add(Pair(x, y))
                }
            }
        }

        emptyPositions.shuffle()

        for (position in emptyPositions) {
            val (x, y) = position
            val randomColor = BlockColor.values().random()
            newBoard[y][x] = newBoard[y][x].copy(color = randomColor)

            if (!canAllShapesBePlaced(newBoard, availableShapes)) {
                newBoard[y][x] = newBoard[y][x].copy(color = null)
                break
            }
        }

        return newBoard
    }

    private fun findPossiblePlacementPositions(
        board: List<List<Block>>,
        shape: Shape
    ): List<Pair<Int, Int>> {
        val positions = mutableListOf<Pair<Int, Int>>()

        for (y in 0 until boardHeight) {
            for (x in 0 until boardWidth) {
                if (canPlaceShapeAtPosition(board, shape, Pair(x, y))) {
                    positions.add(Pair(x, y))
                }
            }
        }

        return positions
    }

    private fun getShapeCoverage(shape: Shape, position: Pair<Int, Int>): Set<Pair<Int, Int>> {
        val coverage = mutableSetOf<Pair<Int, Int>>()
        val (startX, startY) = position

        for (y in 0 until shape.height) {
            for (x in 0 until shape.width) {
                if (shape.matrix[y][x]) {
                    val boardX = startX + x
                    val boardY = startY + y
                    if (boardX in 0 until boardWidth && boardY in 0 until boardHeight) {
                        coverage.add(Pair(boardX, boardY))
                    }
                }
            }
        }

        return coverage
    }

    private fun canAllShapesBePlaced(
        board: List<List<Block>>,
        shapes: List<Shape>
    ): Boolean {
        return canPlaceAllShapesRecursive(board, shapes, 0, mutableSetOf())
    }

    private fun canPlaceAllShapesRecursive(
        board: List<List<Block>>,
        shapes: List<Shape>,
        shapeIndex: Int,
        usedPositions: MutableSet<Pair<Int, Int>>
    ): Boolean {
        if (shapeIndex >= shapes.size) {
            return true
        }

        val shape = shapes[shapeIndex]
        val possiblePositions = findPossiblePlacementPositions(board, shape)

        for (position in possiblePositions) {
            val shapeCoverage = getShapeCoverage(shape, position)

            if (shapeCoverage.any { it in usedPositions }) {
                continue
            }

            usedPositions.addAll(shapeCoverage)

            if (canPlaceAllShapesRecursive(board, shapes, shapeIndex + 1, usedPositions)) {
                return true
            }

            usedPositions.removeAll(shapeCoverage)
        }

        return false
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

    private fun canPlaceShapeAtPosition(
        board: List<List<Block>>,
        shape: Shape,
        position: Pair<Int, Int>
    ): Boolean {
        val (startX, startY) = position

        for (y in 0 until shape.height) {
            for (x in 0 until shape.width) {
                if (shape.matrix[y][x]) {
                    val boardX = startX + x
                    val boardY = startY + y

                    if (boardX < 0 || boardX >= boardWidth || boardY < 0 || boardY >= boardHeight) {
                        return false
                    }

                    if (board[boardY][boardX].color != null) {
                        return false
                    }
                }
            }
        }
        return true
    }
}