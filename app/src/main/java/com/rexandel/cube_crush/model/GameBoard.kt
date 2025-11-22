package com.rexandel.cube_crush.model

import kotlin.random.Random

data class Block(
    val x: Int,
    val y: Int,
    val color: BlockColor? = null
)

enum class BlockColor {
    YELLOW,
    RED,
    BLUE,
    GREEN,
    PURPLE
}

data class Shape(
    val type: ShapeType,
    val color: BlockColor,
    val blocks: List<Pair<Int, Int>>
)

enum class ShapeType {
    SQUARE,
    LINE_1x2,
    LINE_2x1,
    TRIANGLE_3
}

data class GameState(
    val board: List<List<Block>>,
    val availableShapes: List<Shape>,
    val score: Int = 0,
    val highScore: Int = 0,
    val isGameOver: Boolean = false
)

class GameModel(
    private val boardWidth: Int = 8,
    private val boardHeight: Int = 8,
    private val shapesPerMove: Int = 3
) {
    private var _currentState: GameState = createNewGame()

    internal val currentState: GameState get() = _currentState

    fun createNewGame(): GameState {
        val board = List(boardHeight) { y ->
            List(boardWidth) { x ->
                Block(x, y)
            }
        }

        val boardWithInitialBlocks = addInitialRandomBlocks(board)
        val initialShapes = generateUniqueRandomShapes(shapesPerMove)

        return GameState(
            board = boardWithInitialBlocks,
            availableShapes = initialShapes,
            score = 0,
            highScore = 0,
            isGameOver = false
        )
    }

    internal fun placeShape(shapeIndex: Int, position: Pair<Int, Int>): PlaceShapeResult {
        val currentState = _currentState

        if (shapeIndex !in currentState.availableShapes.indices) {
            return PlaceShapeResult.Failure("Invalid shape index")
        }

        val shape = currentState.availableShapes[shapeIndex]

        if (!canPlaceShape(shape, position)) {
            return PlaceShapeResult.Failure("Cannot place shape at position")
        }

        val newBoard = placeShapeOnBoard(shape, position)

        val (boardAfterLineClear, linesCleared) = checkAndClearLines(newBoard)
        val newScore = currentState.score + (linesCleared * 100)

        val newShapes = currentState.availableShapes.mapIndexed { index, existingShape ->
            if (index == shapeIndex) null else existingShape
        }.filterNotNull()

        val allShapesUsed = newShapes.isEmpty()

        val isGameOverAfterMove = checkGameOver(boardAfterLineClear, newShapes)

        _currentState = currentState.copy(
            board = boardAfterLineClear,
            availableShapes = newShapes,
            score = newScore,
            isGameOver = isGameOverAfterMove
        )

        if (allShapesUsed && !isGameOverAfterMove) {
            generateNewShapes()
        }

        return PlaceShapeResult.Success(linesCleared)
    }

    internal fun canPlaceShape(shape: Shape, position: Pair<Int, Int>): Boolean {
        val (startX, startY) = position

        for (block in shape.blocks) {
            val (dx, dy) = block
            val x = startX + dx
            val y = startY + dy

            if (x < 0 || x >= boardWidth || y < 0 || y >= boardHeight) {
                return false
            }

            if (_currentState.board[y][x].color != null) {
                return false
            }
        }
        return true
    }

    internal fun checkGameOver(): Boolean {
        return checkGameOver(_currentState.board, _currentState.availableShapes)
    }

    internal fun restartGame() {
        _currentState = createNewGame()
    }

    internal fun updateHighScore(newHighScore: Int) {
        _currentState = _currentState.copy(highScore = newHighScore)
    }

    fun getCurrentState(): GameState = _currentState

    private fun generateNewShapes() {
        val newShapes = generateUniqueRandomShapes(shapesPerMove)
        val isGameOverAfterNewShapes = checkGameOver(_currentState.board, newShapes)

        _currentState = _currentState.copy(
            availableShapes = newShapes,
            isGameOver = isGameOverAfterNewShapes
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

    private fun generateUniqueRandomShapes(count: Int): List<Shape> {
        val shapes = mutableListOf<Shape>()
        val usedTypes = mutableSetOf<ShapeType>()

        while (shapes.size < count) {
            val shape = createRandomShape()
            if (shape.type !in usedTypes || shapes.size >= ShapeType.values().size) {
                shapes.add(shape)
                usedTypes.add(shape.type)
            }
        }
        return shapes
    }

    private fun createRandomShape(): Shape {
        val color = BlockColor.values().random()
        val shapeType = ShapeType.values().random()

        return when (shapeType) {
            ShapeType.SQUARE -> createSquareShape(color)
            ShapeType.LINE_1x2 -> createLine1x2Shape(color)
            ShapeType.LINE_2x1 -> createLine2x1Shape(color)
            ShapeType.TRIANGLE_3 -> createTriangle3Shape(color)
        }
    }

    private fun createSquareShape(color: BlockColor): Shape {
        return Shape(
            type = ShapeType.SQUARE,
            color = color,
            blocks = listOf(
                Pair(0, 0), Pair(1, 0),
                Pair(0, 1), Pair(1, 1)
            )
        )
    }

    private fun createLine1x2Shape(color: BlockColor): Shape {
        return Shape(
            type = ShapeType.LINE_1x2,
            color = color,
            blocks = listOf(
                Pair(0, 0),
                Pair(0, 1)
            )
        )
    }

    private fun createLine2x1Shape(color: BlockColor): Shape {
        return Shape(
            type = ShapeType.LINE_2x1,
            color = color,
            blocks = listOf(
                Pair(0, 0), Pair(1, 0)
            )
        )
    }

    private fun createTriangle3Shape(color: BlockColor): Shape {
        return Shape(
            type = ShapeType.TRIANGLE_3,
            color = color,
            blocks = listOf(
                Pair(1, 0),
                Pair(0, 1), Pair(1, 1)
            )
        )
    }

    private fun placeShapeOnBoard(shape: Shape, position: Pair<Int, Int>): List<List<Block>> {
        val newBoard = _currentState.board.map { it.toMutableList() }.toMutableList()
        val (startX, startY) = position

        for (block in shape.blocks) {
            val (dx, dy) = block
            val x = startX + dx
            val y = startY + dy

            if (x in 0 until boardWidth && y in 0 until boardHeight) {
                newBoard[y][x] = newBoard[y][x].copy(color = shape.color)
            }
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

    private fun checkGameOver(board: List<List<Block>>, availableShapes: List<Shape>): Boolean {
        if (availableShapes.isEmpty()) {
            return false
        }

        for (shape in availableShapes) {
            for (y in 0 until boardHeight) {
                for (x in 0 until boardWidth) {
                    if (canPlaceShapeAtPosition(board, shape, Pair(x, y))) {
                        return false
                    }
                }
            }
        }

        return true
    }

    private fun canPlaceShapeAtPosition(board: List<List<Block>>, shape: Shape, position: Pair<Int, Int>): Boolean {
        val (startX, startY) = position

        for (block in shape.blocks) {
            val (dx, dy) = block
            val x = startX + dx
            val y = startY + dy

            if (x < 0 || x >= boardWidth || y < 0 || y >= boardHeight) {
                return false
            }

            if (board[y][x].color != null) {
                return false
            }
        }
        return true
    }
}

sealed class PlaceShapeResult {
    data class Success(val linesCleared: Int) : PlaceShapeResult()
    data class Failure(val message: String) : PlaceShapeResult()
}