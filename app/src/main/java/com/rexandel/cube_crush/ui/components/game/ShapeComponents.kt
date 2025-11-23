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
import com.rexandel.cube_crush.model.BlockColor
import com.rexandel.cube_crush.model.Shape
import com.rexandel.cube_crush.model.ShapeType
import kotlin.math.roundToInt

@Composable
fun SquareShape(color: BlockColor, matrix: List<List<Boolean>>) {
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
fun Line1x2Shape(color: BlockColor, matrix: List<List<Boolean>>) {
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
fun Line2x1Shape(color: BlockColor, matrix: List<List<Boolean>>) {
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
fun Triangle3Shape(color: BlockColor, matrix: List<List<Boolean>>) {
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
    val shapeCenterOffset by remember(shape) {
        derivedStateOf {
            calculateShapeCenterOffset(shape)
        }
    }

    val fingerOffset = remember { Offset(0f, -120f) }
    val fixedContainerSize = 100.dp

    val scale by animateFloatAsState(
        targetValue = if (isDragging) 1f else 0.75f,
        animationSpec = tween(durationMillis = 200),
        label = "shape_scale_animation"
    )

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
                .offset {
                    IntOffset(
                        if (isDragging) (dragOffset.x - shapeCenterOffset.x + fingerOffset.x).roundToInt() else 0,
                        if (isDragging) (dragOffset.y - shapeCenterOffset.y + fingerOffset.y).roundToInt() else 0
                    )
                }
        ) {
            when (shape.type) {
                ShapeType.SQUARE -> SquareShape(shape.color, shape.matrix)
                ShapeType.LINE_1x2 -> Line1x2Shape(shape.color, shape.matrix)
                ShapeType.LINE_2x1 -> Line2x1Shape(shape.color, shape.matrix)
                ShapeType.TRIANGLE_3 -> Triangle3Shape(shape.color, shape.matrix)
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