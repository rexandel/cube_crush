package com.rexandel.cube_crush.domain.entities

data class Shape(
    val type: ShapeType,
    val color: BlockColor,
    val matrix: List<List<Boolean>>
) {
    val width: Int get() = matrix.firstOrNull()?.size ?: 0

    val height: Int get() = matrix.size

    val blocks: List<Pair<Int, Int>>
        get() = buildList {
            for (y in matrix.indices) {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        add(Pair(x, y))
                    }
                }
            }
        }
}

enum class ShapeType {
    SQUARE,
    LINE_1x2,
    LINE_2x1,
    TRIANGLE_3
}