package com.rexandel.cube_crush.model

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
            ShapeType.SQUARE -> createSquareShape(color)
            ShapeType.LINE_1x2 -> createLine1x2Shape(color)
            ShapeType.LINE_2x1 -> createLine2x1Shape(color)
            ShapeType.TRIANGLE_3 -> createTriangle3Shape(color)
        }
    }

    private fun createSquareShape(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true, true),
            listOf(true, true)
        )
        return Shape(
            type = ShapeType.SQUARE,
            color = color,
            matrix = matrix
        )
    }

    private fun createLine1x2Shape(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true),
            listOf(true)
        )
        return Shape(
            type = ShapeType.LINE_1x2,
            color = color,
            matrix = matrix
        )
    }

    private fun createLine2x1Shape(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(true, true)
        )
        return Shape(
            type = ShapeType.LINE_2x1,
            color = color,
            matrix = matrix
        )
    }

    private fun createTriangle3Shape(color: BlockColor): Shape {
        val matrix = listOf(
            listOf(false, true),
            listOf(true, true)
        )
        return Shape(
            type = ShapeType.TRIANGLE_3,
            color = color,
            matrix = matrix
        )
    }
}