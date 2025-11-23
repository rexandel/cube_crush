package com.rexandel.cube_crush.model

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