package com.rexandel.cube_crush.ui.components.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.rexandel.cube_crush.R
import com.rexandel.cube_crush.domain.entities.BlockColor
import com.rexandel.cube_crush.domain.entities.Shape
import kotlin.math.roundToInt

@Composable
fun GenericShape(
    color: BlockColor,
    matrix: List<List<Boolean>>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        for (y in matrix.indices) {
            Row {
                for (x in matrix[y].indices) {
                    if (matrix[y][x]) {
                        BlockView(color)
                    } else {
                        Box(modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun BlockView(color: BlockColor) {
    val drawableResId = when (color) {
        BlockColor.YELLOW -> R.drawable.block_yellow
        BlockColor.RED -> R.drawable.block_red
        BlockColor.BLUE -> R.drawable.block_blue
        BlockColor.GREEN -> R.drawable.block_green
        BlockColor.PURPLE -> R.drawable.block_purple
    }

    Box(
        modifier = Modifier
            .size(20.dp)
            .padding(0.8.dp)
    ) {
        Image(
            painter = painterResource(id = drawableResId),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
    }
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

    val fingerOffset = remember { Offset(0f, -150f) }
    val fixedContainerSize = 110.dp

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
            GenericShape(shape.color, shape.matrix)
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
            (totalX / blockCount) * 22f,
            (totalY / blockCount) * 22f
        )
    }
}