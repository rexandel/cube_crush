package com.rexandel.cube_crush.ui.components.game

import androidx.compose.ui.geometry.Offset
import com.rexandel.cube_crush.domain.entities.Shape
import kotlin.math.roundToInt

object DragUtils {

    fun findSnapPosition(
        dragCenter: Offset,
        boardPosition: Offset,
        boardSize: Float,
        shape: Shape
    ): Pair<Int, Int>? {
        val boardRight = boardPosition.x + 8 * boardSize
        val boardBottom = boardPosition.y + 8 * boardSize

        val captureMargin = boardSize * 3.0f

        if (dragCenter.x < boardPosition.x - captureMargin ||
            dragCenter.x > boardRight + captureMargin ||
            dragCenter.y < boardPosition.y - captureMargin ||
            dragCenter.y > boardBottom + captureMargin) {
            return null
        }

        val shapeWidth = shape.width
        val shapeHeight = shape.height

        val shapeCenterX = calculateShapeCenter(shape, isHorizontal = true)
        val shapeCenterY = calculateShapeCenter(shape, isHorizontal = false)

        var bestPosition: Pair<Int, Int>? = null
        var minDistance = Float.MAX_VALUE
        val snapThreshold = boardSize * 2.5f

        for (y in 0..8 - shapeHeight) {
            for (x in 0..8 - shapeWidth) {
                val candidateCenterX = boardPosition.x + (x + shapeCenterX) * boardSize
                val candidateCenterY = boardPosition.y + (y + shapeCenterY) * boardSize

                val dx = dragCenter.x - candidateCenterX
                val dy = dragCenter.y - candidateCenterY
                val distance = kotlin.math.sqrt(dx * dx + dy * dy)

                if (distance < minDistance && distance < snapThreshold) {
                    minDistance = distance
                    bestPosition = Pair(x, y)
                }
            }
        }

        return bestPosition
    }

    private fun calculateShapeCenter(shape: Shape, isHorizontal: Boolean): Float {
        val matrix = shape.matrix
        var total = 0f
        var blockCount = 0

        for (y in matrix.indices) {
            for (x in matrix[y].indices) {
                if (matrix[y][x]) {
                    total += if (isHorizontal) x + 0.5f else y + 0.5f
                    blockCount++
                }
            }
        }

        return if (blockCount > 0) total / blockCount else 0f
    }


}