package com.rexandel.cube_crush.domain.game

import com.rexandel.cube_crush.domain.entities.BlockColor
import com.rexandel.cube_crush.domain.entities.Shape
import com.rexandel.cube_crush.domain.entities.ShapeType

object ShapeFactory {
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

    private fun createBlock1x1Shape(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true)
        )
        return Shape(
            type = ShapeType.BLOCK_1x1,
            color = color,
            matrix = matrix
        )
    }

    private fun createRect1x2Shape(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true),
            listOf(true)
        )
        return Shape(
            type = ShapeType.RECT_1x2,
            color = color,
            matrix = matrix
        )
    }

    private fun createRect2x1Shape(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true, true)
        )
        return Shape(
            type = ShapeType.RECT_2x1,
            color = color,
            matrix = matrix
        )
    }

    private fun createRect1x3Shape(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true),
            listOf(true),
            listOf(true)
        )
        return Shape(
            type = ShapeType.RECT_1x3,
            color = color,
            matrix = matrix
        )
    }

    private fun createRect3x1Shape(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true, true, true)
        )
        return Shape(
            type = ShapeType.RECT_3x1,
            color = color,
            matrix = matrix
        )
    }

    private fun createRect1x4Shape(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true),
            listOf(true),
            listOf(true),
            listOf(true)
        )
        return Shape(
            type = ShapeType.RECT_1x4,
            color = color,
            matrix = matrix
        )
    }

    private fun createRect4x1Shape(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true, true, true, true)
        )
        return Shape(
            type = ShapeType.RECT_4x1,
            color = color,
            matrix = matrix
        )
    }

    private fun createRect1x5Shape(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true),
            listOf(true),
            listOf(true),
            listOf(true),
            listOf(true)
        )
        return Shape(
            type = ShapeType.RECT_1x5,
            color = color,
            matrix = matrix
        )
    }

    private fun createRect5x1Shape(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true, true, true, true, true)
        )
        return Shape(
            type = ShapeType.RECT_5x1,
            color = color,
            matrix = matrix
        )
    }

    private fun createSquare2x2Shape(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true, true),
            listOf(true, true)
        )
        return Shape(
            type = ShapeType.SQUARE_2x2,
            color = color,
            matrix = matrix
        )
    }

    private fun createSquare3x3Shape(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true, true, true),
            listOf(true, true, true),
            listOf(true, true, true)
        )
        return Shape(
            type = ShapeType.SQUARE_3x3,
            color = color,
            matrix = matrix
        )
    }

    private fun createLShape2x2TopLeft(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(false, true),
            listOf(true, true)
        )
        return Shape(
            type = ShapeType.L_SHAPE_2x2_TOP_LEFT,
            color = color,
            matrix = matrix
        )
    }

    private fun createLShape2x2TopRight(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true, false),
            listOf(true, true)
        )
        return Shape(
            type = ShapeType.L_SHAPE_2x2_TOP_RIGHT,
            color = color,
            matrix = matrix
        )
    }

    private fun createLShape2x3TopLeft(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true, false),
            listOf(true, false),
            listOf(true, true)
        )
        return Shape(
            type = ShapeType.L_SHAPE_2x3_TOP_LEFT,
            color = color,
            matrix = matrix
        )
    }

    private fun createLShape2x3TopRight(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(false, true),
            listOf(false, true),
            listOf(true, true)
        )
        return Shape(
            type = ShapeType.L_SHAPE_2x3_TOP_RIGHT,
            color = color,
            matrix = matrix
        )
    }

    private fun createLShape3x3TopLeft(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true, false, false),
            listOf(true, false, false),
            listOf(true, true, true)
        )
        return Shape(
            type = ShapeType.L_SHAPE_3x3_TOP_LEFT,
            color = color,
            matrix = matrix
        )
    }

    private fun createLShape3x3TopRight(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(false, false, true),
            listOf(false, false, true),
            listOf(true, true, true)
        )
        return Shape(
            type = ShapeType.L_SHAPE_3x3_TOP_RIGHT,
            color = color,
            matrix = matrix
        )
    }

    private fun createLine3HorizontalShape(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true, true, true)
        )
        return Shape(
            type = ShapeType.LINE_3_HORIZONTAL,
            color = color,
            matrix = matrix
        )
    }

    private fun createLine4HorizontalShape(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true, true, true, true)
        )
        return Shape(
            type = ShapeType.LINE_4_HORIZONTAL,
            color = color,
            matrix = matrix
        )
    }

    private fun createLine5HorizontalShape(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true, true, true, true, true)
        )
        return Shape(
            type = ShapeType.LINE_5_HORIZONTAL,
            color = color,
            matrix = matrix
        )
    }

    private fun createLine3VerticalShape(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true),
            listOf(true),
            listOf(true)
        )
        return Shape(
            type = ShapeType.LINE_3_VERTICAL,
            color = color,
            matrix = matrix
        )
    }

    private fun createLine4VerticalShape(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true),
            listOf(true),
            listOf(true),
            listOf(true)
        )
        return Shape(
            type = ShapeType.LINE_4_VERTICAL,
            color = color,
            matrix = matrix
        )
    }

    private fun createLine5VerticalShape(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true),
            listOf(true),
            listOf(true),
            listOf(true),
            listOf(true)
        )
        return Shape(
            type = ShapeType.LINE_5_VERTICAL,
            color = color,
            matrix = matrix
        )
    }
}