package com.rexandel.cube_crush.ui.components.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.rexandel.cube_crush.domain.entities.BlockColor
import com.rexandel.cube_crush.domain.entities.Shape
import com.rexandel.cube_crush.domain.entities.ShapeType
import kotlin.math.roundToInt

@Composable
fun Block1x1Shape(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun Rect1x2Shape(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun Rect2x1Shape(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun Rect1x3Shape(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun Rect3x1Shape(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun Rect1x4Shape(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun Rect4x1Shape(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun Rect1x5Shape(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun Rect5x1Shape(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun Square2x2Shape(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun Square3x3Shape(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun LShape2x2TopLeft(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun LShape2x2TopRight(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun LShape2x3TopLeft(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun LShape2x3TopRight(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun LShape3x3TopLeft(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun LShape3x3TopRight(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun Line3HorizontalShape(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun Line4HorizontalShape(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun Line5HorizontalShape(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun Line3VerticalShape(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun Line4VerticalShape(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun Line5VerticalShape(color: BlockColor, matrix: List<List<Boolean>>) {
    Column {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun BlockView(color: BlockColor) {
    val colorValue = when (color) {
        BlockColor.YELLOW -> Color.Yellow
        BlockColor.RED -> Color.Red
        BlockColor.BLUE -> Color.Blue
        BlockColor.GREEN -> Color.Green
        BlockColor.PURPLE -> Color.Magenta
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(2.dp)
            .background(
                color = colorValue,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
            )
    )
}

@Composable
fun DraggableShape(
    shape: Shape,
    shapeIndex: Int,
    isDragging: Boolean,
    isActive: Boolean,
    dragOffset: Offset,
    onDragStart: (Int) -> Unit,
    onDrag: (Int, Offset) -> Unit,
    onDragEnd: (Int) -> Unit,
    onPositioned: (Offset) -> Unit
) {
    val shapeCenterOffset = remember(shape) {
        calculateShapeCenterOffset(shape)
    }

    val fingerOffset = remember { Offset(0f, -120f) }
    val fixedContainerSize = 100.dp

    val scale by animateFloatAsState(
        targetValue = if (isDragging) 1f else 0.75f,
        animationSpec = tween(durationMillis = 200),
        label = "shape_scale_animation"
    )

    val offsetX by remember(dragOffset, isDragging, shapeCenterOffset) {
        derivedStateOf {
            if (isDragging) (dragOffset.x - shapeCenterOffset.x + fingerOffset.x).roundToInt() else 0
        }
    }

    val offsetY by remember(dragOffset, isDragging, shapeCenterOffset) {
        derivedStateOf {
            if (isDragging) (dragOffset.y - shapeCenterOffset.y + fingerOffset.y).roundToInt() else 0
        }
    }

    Box(
        modifier = Modifier
            .size(fixedContainerSize)
            .onGloballyPositioned { coordinates ->
                onPositioned(coordinates.positionInRoot())
            }
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .scale(scale)
                .offset { IntOffset(offsetX, offsetY) }
        ) {
            when (shape.type) {
                ShapeType.BLOCK_1x1 -> Block1x1Shape(shape.color, shape.matrix)

                ShapeType.RECT_1x2 -> Rect1x2Shape(shape.color, shape.matrix)
                ShapeType.RECT_2x1 -> Rect2x1Shape(shape.color, shape.matrix)
                ShapeType.RECT_1x3 -> Rect1x3Shape(shape.color, shape.matrix)
                ShapeType.RECT_3x1 -> Rect3x1Shape(shape.color, shape.matrix)
                ShapeType.RECT_1x4 -> Rect1x4Shape(shape.color, shape.matrix)
                ShapeType.RECT_4x1 -> Rect4x1Shape(shape.color, shape.matrix)
                ShapeType.RECT_1x5 -> Rect1x5Shape(shape.color, shape.matrix)
                ShapeType.RECT_5x1 -> Rect5x1Shape(shape.color, shape.matrix)

                ShapeType.SQUARE_2x2 -> Square2x2Shape(shape.color, shape.matrix)
                ShapeType.SQUARE_3x3 -> Square3x3Shape(shape.color, shape.matrix)

                ShapeType.L_SHAPE_2x2_TOP_LEFT -> LShape2x2TopLeft(shape.color, shape.matrix)
                ShapeType.L_SHAPE_2x2_TOP_RIGHT -> LShape2x2TopRight(shape.color, shape.matrix)
                ShapeType.L_SHAPE_2x3_TOP_LEFT -> LShape2x3TopLeft(shape.color, shape.matrix)
                ShapeType.L_SHAPE_2x3_TOP_RIGHT -> LShape2x3TopRight(shape.color, shape.matrix)
                ShapeType.L_SHAPE_3x3_TOP_LEFT -> LShape3x3TopLeft(shape.color, shape.matrix)
                ShapeType.L_SHAPE_3x3_TOP_RIGHT -> LShape3x3TopRight(shape.color, shape.matrix)

                ShapeType.LINE_3_HORIZONTAL -> Line3HorizontalShape(shape.color, shape.matrix)
                ShapeType.LINE_4_HORIZONTAL -> Line4HorizontalShape(shape.color, shape.matrix)
                ShapeType.LINE_5_HORIZONTAL -> Line5HorizontalShape(shape.color, shape.matrix)
                ShapeType.LINE_3_VERTICAL -> Line3VerticalShape(shape.color, shape.matrix)
                ShapeType.LINE_4_VERTICAL -> Line4VerticalShape(shape.color, shape.matrix)
                ShapeType.LINE_5_VERTICAL -> Line5VerticalShape(shape.color, shape.matrix)
            }
        }

        if (isActive) {
            Box(
                modifier = Modifier
                    .size(fixedContainerSize)
                    .background(Color.Transparent, CircleShape)
                    .pointerInput(shapeIndex) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                onDragStart(shapeIndex)
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                onDrag(shapeIndex, dragAmount)
                            },
                            onDragEnd = {
                                onDragEnd(shapeIndex)
                            },
                            onDragCancel = {
                                onDragEnd(shapeIndex)
                            }
                        )
                    }
            )
        }
    }
}

private fun calculateShapeCenterOffset(shape: Shape): Offset {
    val matrix = shape.matrix
    var totalX = 0f
    var totalY = 0f
    var blockCount = 0

    for (y in matrix.indices) {
        val row = matrix[y]
        for (x in row.indices) {
            if (row[x]) {
                totalX += x + 0.5f
                totalY += y + 0.5f
                blockCount++
            }
        }
    }

    return if (blockCount == 0) {
        Offset.Zero
    } else {
        Offset(
            (totalX / blockCount) * 40f,
            (totalY / blockCount) * 40f
        )
    }
}