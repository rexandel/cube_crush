package com.rexandel.cube_crush.model

class GameModel(
    private val boardWidth: Int = 8,
    private val boardHeight: Int = 8,
    private val shapesPerMove: Int = 3
) {
    private var _currentState: GameState
    private val boardManager: BoardManager = BoardManager(boardWidth, boardHeight)
    private val shapeFactory = ShapeFactory

    init {
        _currentState = createNewGame()
    }

    internal val currentState: GameState get() = _currentState

    fun createNewGame(): GameState {
        val board = boardManager.createEmptyBoard()
        val boardWithInitialBlocks = boardManager.addInitialRandomBlocks(board)
        val initialShapes = shapeFactory.generateUniqueRandomShapes(shapesPerMove)

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

        val newBoard = boardManager.placeShapeOnBoard(currentState.board, shape, position)

        val (boardAfterLineClear, linesCleared) = boardManager.checkAndClearLines(newBoard)
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
        _currentState = createNewGame()
    }

    internal fun updateHighScore(newHighScore: Int) {
        _currentState = _currentState.copy(highScore = newHighScore)
    }

    fun getCurrentState(): GameState = _currentState

    private fun generateNewShapes() {
        val newShapes = shapeFactory.generateUniqueRandomShapes(shapesPerMove)
        val isGameOverAfterNewShapes = checkGameOver(_currentState.board, newShapes)

        _currentState = _currentState.copy(
            availableShapes = newShapes,
            isGameOver = isGameOverAfterNewShapes
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