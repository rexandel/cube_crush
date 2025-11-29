package com.rexandel.cube_crush.domain.game

import com.rexandel.cube_crush.domain.entities.Block
import com.rexandel.cube_crush.domain.entities.GameState
import com.rexandel.cube_crush.domain.entities.PlaceShapeResult
import com.rexandel.cube_crush.domain.entities.Shape

class GameModel(
    private val boardWidth: Int = 8,
    private val boardHeight: Int = 8,
    private val shapesPerMove: Int = 3
) {
    private var _currentState: GameState
    private val boardManager: BoardManager = BoardManager(boardWidth, boardHeight)
    private val shapeFactory = ShapeFactory
    private var comboCounter: Int = 0
    private var hasClearedLineThisTurn: Boolean = false

    init {
        _currentState = createNewGame()
    }

    internal val currentState: GameState get() = _currentState

    fun createNewGame(highScore: Int = 0): GameState {
        val board = boardManager.createEmptyBoard()
        val initialShapes = shapeFactory.generateSmartShapes(board, shapesPerMove)
        val boardWithInitialBlocks = boardManager.addInitialRandomBlocks(board, initialShapes)
        comboCounter = 0
        hasClearedLineThisTurn = false

        return GameState(
            board = boardWithInitialBlocks,
            availableShapes = initialShapes,
            score = 0,
            highScore = highScore,
            isGameOver = false,
            comboCount = 0
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

        val newBoard = boardManager.placeShapeOnBoard(currentState.board, shape, position)

        val (boardAfterLineClear, linesCleared) = boardManager.checkAndClearLines(newBoard)

        val baseScore = linesCleared * 100

        val comboMultiplier = if (comboCounter > 0) 1 + (comboCounter * 0.5) else 1.0
        val scoreWithCombo = (baseScore * comboMultiplier).toInt()

        if (linesCleared > 0) {
            hasClearedLineThisTurn = true
            comboCounter++
        }

        val newScore = currentState.score + scoreWithCombo
        val newHighScore = maxOf(currentState.highScore, newScore)

        val newShapes = currentState.availableShapes.mapIndexed { index, existingShape ->
            if (index == shapeIndex) null else existingShape
        }.filterNotNull()

        val allShapesUsed = newShapes.isEmpty()

        val isGameOverAfterMove = checkGameOver(boardAfterLineClear, newShapes)

        _currentState = currentState.copy(
            board = boardAfterLineClear,
            availableShapes = newShapes,
            score = newScore,
            highScore = newHighScore,
            isGameOver = isGameOverAfterMove,
            comboCount = comboCounter
        )

        if (allShapesUsed && !isGameOverAfterMove) {
            generateNewSmartShapes()
        }

        return PlaceShapeResult.Success(linesCleared, scoreWithCombo, comboCounter)
    }

    internal fun canPlaceShape(shape: Shape, position: Pair<Int, Int>): Boolean {
        val (startX, startY) = position

        for (y in 0 until shape.height) {
            for (x in 0 until shape.width) {
                if (shape.matrix[y][x]) {
                    val boardX = startX + x
                    val boardY = startY + y

                    if (boardX < 0 || boardX >= boardWidth || boardY < 0 || boardY >= boardHeight) {
                        return false
                    }

                    if (_currentState.board[boardY][boardX].color != null) {
                        return false
                    }
                }
            }
        }
        return true
    }

    internal fun checkGameOver(): Boolean {
        return checkGameOver(_currentState.board, _currentState.availableShapes)
    }

    internal fun restartGame() {
        val currentHighScore = _currentState.highScore
        _currentState = createNewGame(highScore = currentHighScore)
    }

    internal fun updateHighScore(newHighScore: Int) {
        _currentState = _currentState.copy(highScore = newHighScore)
    }

    fun getCurrentState(): GameState = _currentState

    private fun generateNewSmartShapes() {
        val newShapes = shapeFactory.generateSmartShapes(_currentState.board, shapesPerMove)
        val isGameOverAfterNewShapes = checkGameOver(_currentState.board, newShapes)

        if (!hasClearedLineThisTurn) {
            comboCounter = 0
        }
        hasClearedLineThisTurn = false

        _currentState = _currentState.copy(
            availableShapes = newShapes,
            isGameOver = isGameOverAfterNewShapes,
            comboCount = comboCounter
        )
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