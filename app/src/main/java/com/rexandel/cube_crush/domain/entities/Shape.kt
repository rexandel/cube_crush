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
    BLOCK_1x1,

    RECT_1x2,
    RECT_2x1,
    RECT_1x3,
    RECT_3x1,
    RECT_1x4,
    RECT_4x1,
    RECT_1x5,
    RECT_5x1,

    SQUARE_2x2,
    SQUARE_3x3,

    L_SHAPE_2x2_TOP_LEFT,
    L_SHAPE_2x2_TOP_RIGHT,
    L_SHAPE_2x3_TOP_LEFT,
    L_SHAPE_2x3_TOP_RIGHT,
    L_SHAPE_3x3_TOP_LEFT,
    L_SHAPE_3x3_TOP_RIGHT,

    LINE_3_HORIZONTAL,
    LINE_4_HORIZONTAL,
    LINE_5_HORIZONTAL,
    LINE_3_VERTICAL,
    LINE_4_VERTICAL,
    LINE_5_VERTICAL
}