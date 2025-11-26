package com.rexandel.cube_crush.domain.game

import com.rexandel.cube_crush.domain.entities.Block
import com.rexandel.cube_crush.domain.entities.BlockColor
import com.rexandel.cube_crush.domain.entities.Shape
import com.rexandel.cube_crush.domain.entities.ShapeType
import kotlin.random.Random

object ShapeFactory {
    private const val MAX_GENERATION_ATTEMPTS = 50

    fun generateSmartShapes(board: List<List<Block>>, count: Int): List<Shape> {
        val shapes = mutableListOf<Shape>()
        val usedTypes = mutableSetOf<ShapeType>()
        var attempts = 0

        while (shapes.size < count && attempts < MAX_GENERATION_ATTEMPTS) {
            val shape = createRandomShape()

            if (shape.type in usedTypes && usedTypes.size < ShapeType.values().size) {
                attempts++
                continue
            }

            if (canShapeBePlaced(board, shape)) {
                if (canShapeClearLines(board, shape) || shapes.size < count - 1) {
                    shapes.add(shape)
                    usedTypes.add(shape.type)
                    attempts = 0
                }
            }
            attempts++
        }

        if (shapes.size < count) {
            val backupShapes = generateFallbackShapes(board, count - shapes.size, usedTypes)
            shapes.addAll(backupShapes)
        }

        return shapes
    }

    fun generateUniqueRandomShapes(count: Int): List<Shape> {
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

    private fun generateFallbackShapes(
        board: List<List<Block>>,
        count: Int,
        excludedTypes: Set<ShapeType>
    ): List<Shape> {
        val shapes = mutableListOf<Shape>()
        var attempts = 0

        while (shapes.size < count && attempts < MAX_GENERATION_ATTEMPTS) {
            val shape = createRandomShape()

            if (shape.type in excludedTypes && excludedTypes.size < ShapeType.values().size) {
                attempts++
                continue
            }

            if (canShapeBePlaced(board, shape)) {
                shapes.add(shape)
                attempts = 0
            }
            attempts++
        }

        return shapes
    }

    private fun canShapeBePlaced(board: List<List<Block>>, shape: Shape): Boolean {
        val boardWidth = board[0].size
        val boardHeight = board.size

        for (y in 0 until boardHeight) {
            for (x in 0 until boardWidth) {
                if (canPlaceShapeAtPosition(board, shape, Pair(x, y))) {
                    return true
                }
            }
        }
        return false
    }

    private fun canShapeClearLines(board: List<List<Block>>, shape: Shape): Boolean {
        val boardWidth = board[0].size
        val boardHeight = board.size

        for (y in 0 until boardHeight) {
            for (x in 0 until boardWidth) {
                if (canPlaceShapeAtPosition(board, shape, Pair(x, y))) {
                    val tempBoard = placeShapeOnTempBoard(board, shape, Pair(x, y))
                    if (wouldClearLines(tempBoard, shape, Pair(x, y))) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun wouldClearLines(board: List<List<Block>>, shape: Shape, position: Pair<Int, Int>): Boolean {
        val tempBoard = placeShapeOnTempBoard(board, shape, position)
        val boardWidth = tempBoard[0].size
        val boardHeight = tempBoard.size

        for (y in 0 until boardHeight) {
            if (tempBoard[y].all { it.color != null }) {
                return true
            }
        }

        for (x in 0 until boardWidth) {
            var columnFull = true
            for (y in 0 until boardHeight) {
                if (tempBoard[y][x].color == null) {
                    columnFull = false
                    break
                }
            }
            if (columnFull) {
                return true
            }
        }

        return false
    }

    private fun placeShapeOnTempBoard(
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

                    if (boardX in 0 until board[0].size && boardY in 0 until board.size) {
                        newBoard[boardY][boardX] = newBoard[boardY][boardX].copy(color = shape.color)
                    }
                }
            }
        }
        return newBoard
    }

    private fun canPlaceShapeAtPosition(
        board: List<List<Block>>,
        shape: Shape,
        position: Pair<Int, Int>
    ): Boolean {
        val (startX, startY) = position
        val boardWidth = board[0].size
        val boardHeight = board.size

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

    private fun createRandomShape(): Shape {
        val color = BlockColor.values().random()
        val shapeType = ShapeType.values().random()

        return when (shapeType) {
            ShapeType.BLOCK_1x1 -> createBlock1x1Shape(color)

            ShapeType.RECT_1x2 -> createRect1x2Shape(color)
            ShapeType.RECT_2x1 -> createRect2x1Shape(color)
            ShapeType.RECT_1x3 -> createRect1x3Shape(color)
            ShapeType.RECT_3x1 -> createRect3x1Shape(color)
            ShapeType.RECT_1x4 -> createRect1x4Shape(color)
            ShapeType.RECT_4x1 -> createRect4x1Shape(color)
            ShapeType.RECT_1x5 -> createRect1x5Shape(color)
            ShapeType.RECT_5x1 -> createRect5x1Shape(color)

            ShapeType.SQUARE_2x2 -> createSquare2x2Shape(color)
            ShapeType.SQUARE_3x3 -> createSquare3x3Shape(color)

            ShapeType.L_SHAPE_2x2_TOP_LEFT -> createLShape2x2TopLeft(color)
            ShapeType.L_SHAPE_2x2_TOP_RIGHT -> createLShape2x2TopRight(color)
            ShapeType.L_SHAPE_2x3_TOP_LEFT -> createLShape2x3TopLeft(color)
            ShapeType.L_SHAPE_2x3_TOP_RIGHT -> createLShape2x3TopRight(color)
            ShapeType.L_SHAPE_3x3_TOP_LEFT -> createLShape3x3TopLeft(color)
            ShapeType.L_SHAPE_3x3_TOP_RIGHT -> createLShape3x3TopRight(color)

            ShapeType.LINE_3_HORIZONTAL -> createLine3HorizontalShape(color)
            ShapeType.LINE_4_HORIZONTAL -> createLine4HorizontalShape(color)
            ShapeType.LINE_5_HORIZONTAL -> createLine5HorizontalShape(color)
            ShapeType.LINE_3_VERTICAL -> createLine3VerticalShape(color)
            ShapeType.LINE_4_VERTICAL -> createLine4VerticalShape(color)
            ShapeType.LINE_5_VERTICAL -> createLine5VerticalShape(color)
        }
    }

    // Все методы создания фигур остаются без изменений
    private fun createBlock1x1Shape(color: BlockColor): Shape {
        val matrix = listOf(listOf(true))
        return Shape(ShapeType.BLOCK_1x1, color, matrix)
    }

    private fun createRect1x2Shape(color: BlockColor): Shape {
        val matrix = listOf(listOf(true), listOf(true))
        return Shape(ShapeType.RECT_1x2, color, matrix)
    }

    private fun createRect2x1Shape(color: BlockColor): Shape {
        val matrix = listOf(listOf(true, true))
        return Shape(ShapeType.RECT_2x1, color, matrix)
    }

    private fun createRect1x3Shape(color: BlockColor): Shape {
        val matrix = listOf(listOf(true), listOf(true), listOf(true))
        return Shape(ShapeType.RECT_1x3, color, matrix)
    }

    private fun createRect3x1Shape(color: BlockColor): Shape {
        val matrix = listOf(listOf(true, true, true))
        return Shape(ShapeType.RECT_3x1, color, matrix)
    }

    private fun createRect1x4Shape(color: BlockColor): Shape {
        val matrix = listOf(listOf(true), listOf(true), listOf(true), listOf(true))
        return Shape(ShapeType.RECT_1x4, color, matrix)
    }

    private fun createRect4x1Shape(color: BlockColor): Shape {
        val matrix = listOf(listOf(true, true, true, true))
        return Shape(ShapeType.RECT_4x1, color, matrix)
    }

    private fun createRect1x5Shape(color: BlockColor): Shape {
        val matrix = listOf(listOf(true), listOf(true), listOf(true), listOf(true), listOf(true))
        return Shape(ShapeType.RECT_1x5, color, matrix)
    }

    private fun createRect5x1Shape(color: BlockColor): Shape {
        val matrix = listOf(listOf(true, true, true, true, true))
        return Shape(ShapeType.RECT_5x1, color, matrix)
    }

    private fun createSquare2x2Shape(color: BlockColor): Shape {
        val matrix = listOf(listOf(true, true), listOf(true, true))
        return Shape(ShapeType.SQUARE_2x2, color, matrix)
    }

    private fun createSquare3x3Shape(color: BlockColor): Shape {
        val matrix = listOf(listOf(true, true, true), listOf(true, true, true), listOf(true, true, true))
        return Shape(ShapeType.SQUARE_3x3, color, matrix)
    }

    private fun createLShape2x2TopLeft(color: BlockColor): Shape {
        val matrix = listOf(listOf(false, true), listOf(true, true))
        return Shape(ShapeType.L_SHAPE_2x2_TOP_LEFT, color, matrix)
    }

    private fun createLShape2x2TopRight(color: BlockColor): Shape {
        val matrix = listOf(listOf(true, false), listOf(true, true))
        return Shape(ShapeType.L_SHAPE_2x2_TOP_RIGHT, color, matrix)
    }

    private fun createLShape2x3TopLeft(color: BlockColor): Shape {
        val matrix = listOf(listOf(true, false), listOf(true, false), listOf(true, true))
        return Shape(ShapeType.L_SHAPE_2x3_TOP_LEFT, color, matrix)
    }

    private fun createLShape2x3TopRight(color: BlockColor): Shape {
        val matrix = listOf(listOf(false, true), listOf(false, true), listOf(true, true))
        return Shape(ShapeType.L_SHAPE_2x3_TOP_RIGHT, color, matrix)
    }

    private fun createLShape3x3TopLeft(color: BlockColor): Shape {
        val matrix = listOf(listOf(true, false, false), listOf(true, false, false), listOf(true, true, true))
        return Shape(ShapeType.L_SHAPE_3x3_TOP_LEFT, color, matrix)
    }

    private fun createLShape3x3TopRight(color: BlockColor): Shape {
        val matrix = listOf(listOf(false, false, true), listOf(false, false, true), listOf(true, true, true))
        return Shape(ShapeType.L_SHAPE_3x3_TOP_RIGHT, color, matrix)
    }

    private fun createLine3HorizontalShape(color: BlockColor): Shape {
        val matrix = listOf(listOf(true, true, true))
        return Shape(ShapeType.LINE_3_HORIZONTAL, color, matrix)
    }

    private fun createLine4HorizontalShape(color: BlockColor): Shape {
        val matrix = listOf(listOf(true, true, true, true))
        return Shape(ShapeType.LINE_4_HORIZONTAL, color, matrix)
    }

    private fun createLine5HorizontalShape(color: BlockColor): Shape {
        val matrix = listOf(listOf(true, true, true, true, true))
        return Shape(ShapeType.LINE_5_HORIZONTAL, color, matrix)
    }

    private fun createLine3VerticalShape(color: BlockColor): Shape {
        val matrix = listOf(listOf(true), listOf(true), listOf(true))
        return Shape(ShapeType.LINE_3_VERTICAL, color, matrix)
    }

    private fun createLine4VerticalShape(color: BlockColor): Shape {
        val matrix = listOf(listOf(true), listOf(true), listOf(true), listOf(true))
        return Shape(ShapeType.LINE_4_VERTICAL, color, matrix)
    }

    private fun createLine5VerticalShape(color: BlockColor): Shape {
        val matrix = listOf(listOf(true), listOf(true), listOf(true), listOf(true), listOf(true))
        return Shape(ShapeType.LINE_5_VERTICAL, color, matrix)
    }
}