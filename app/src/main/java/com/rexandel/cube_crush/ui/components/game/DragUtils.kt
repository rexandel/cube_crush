package com.rexandel.cube_crush.ui.components.game

import androidx.compose.ui.geometry.Offset
import com.rexandel.cube_crush.domain.entities.Shape
import kotlin.math.roundToInt

object DragUtils {

    fun findSnapPosition(
        absoluteX: Float,
        absoluteY: Float,
        boardPosition: Offset,
        boardSize: Float,
        shape: Shape
    ): Pair<Int, Int>? {
        val boardRight = boardPosition.x + 8 * boardSize
        val boardBottom = boardPosition.y + 8 * boardSize

        val captureMargin = boardSize * 0.5f

        if (absoluteX < boardPosition.x - captureMargin ||
            absoluteX > boardRight + captureMargin ||
            absoluteY < boardPosition.y - captureMargin ||
            absoluteY > boardBottom + captureMargin) {
            return null
        }

        val shapeWidth = shape.width
        val shapeHeight = shape.height

        val relativeX = absoluteX - boardPosition.x
        val relativeY = absoluteY - boardPosition.y

        val boardX = (relativeX / boardSize).roundToInt()
        val boardY = (relativeY / boardSize).roundToInt()

        val clampedX = boardX.coerceIn(0, 8 - shapeWidth)
        val clampedY = boardY.coerceIn(0, 8 - shapeHeight)

        return Pair(clampedX, clampedY)
    }

    fun calculateShapeCenterOffset(shape: Shape): Offset {
        val matrix = shape.matrix
        var totalX = 0f
        var totalY = 0f
        var blockCount = 0

        for (y in matrix.indices) {
            for (x in matrix[y].indices) {
                if (matrix[y][x]) {
                    totalX += x + 0.5f
                    totalY += y + 0.5f
                    blockCount++
                }
            }
        }

        if (blockCount == 0) return Offset.Zero

        val centerX = totalX / blockCount
        val centerY = totalY / blockCount

        return Offset(centerX * 40f, centerY * 40f)
    }

    fun calculateAbsoluteShapePosition(
        shapeStartPosition: Offset,
        dragOffset: Offset,
        shapeCenterOffset: Offset,
        fingerOffset: Offset = Offset(0f, -200f)
    ): Offset {
        val absolutePosition = shapeStartPosition + dragOffset
        return Offset(
            absolutePosition.x - shapeCenterOffset.x + fingerOffset.x,
            absolutePosition.y - shapeCenterOffset.y + fingerOffset.y
        )
    }
}