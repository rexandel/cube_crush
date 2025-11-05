package com.rexandel.cube_crush.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rexandel.cube_crush.model.Block
import com.rexandel.cube_crush.model.BlockColor
import com.rexandel.cube_crush.model.Shape
import com.rexandel.cube_crush.model.ShapeType
import com.rexandel.cube_crush.viewmodel.GameViewModel
import kotlin.math.roundToInt

@Composable
fun GameScreen() {
    val gameViewModel: GameViewModel = viewModel()
    val gameState = gameViewModel.gameState

    var draggingShapeIndex by remember { mutableStateOf<Int?>(null) }
    var dragOffsets by remember { mutableStateOf(List(3) { Offset.Zero }) }
    var boardPosition by remember { mutableStateOf(Offset.Zero) }
    var boardSize by remember { mutableStateOf(0.dp) }
    var shapeStartPositions by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var snapPreviewPosition by remember { mutableStateOf<Pair<Int, Int>?>(null) }

    if (gameState.isGameOver) {
        GameOverDialog(
            score = gameState.score,
            highScore = gameState.highScore,
            onRestart = { gameViewModel.restartGame() }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Рекорд: ${gameState.highScore}",
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    // TODO: Реализовать паузу
                }
            ) {
                Text("Пауза")
            }
        }

        Text(
            text = gameState.score.toString(),
            fontSize = 48.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(16.dp)
                .onGloballyPositioned { coordinates ->
                    boardPosition = coordinates.positionInRoot()
                    boardSize = with(coordinates.size) { (width / 8).dp }
                }
        ) {
            GameBoard(
                board = gameState.board,
                snapPreviewPosition = snapPreviewPosition,
                draggingShape = draggingShapeIndex?.let { gameState.availableShapes.getOrNull(it) },
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Suggested shapes
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            gameState.availableShapes.forEachIndexed { index, shape ->
                if (shape != null) {
                    DraggableShape(
                        shape = shape,
                        shapeIndex = index,
                        isDragging = draggingShapeIndex == index,
                        isActive = draggingShapeIndex == null || draggingShapeIndex == index,
                        dragOffset = dragOffsets.getOrNull(index) ?: Offset.Zero,
                        onDragStart = { shapeIndex ->
                            draggingShapeIndex = shapeIndex
                            dragOffsets = dragOffsets.mapIndexed { i, offset ->
                                if (i == shapeIndex) Offset.Zero else offset
                            }
                            snapPreviewPosition = null
                        },
                        onDrag = { shapeIndex, dragAmount ->
                            dragOffsets = dragOffsets.mapIndexed { i, currentOffset ->
                                if (i == shapeIndex) currentOffset + dragAmount else currentOffset
                            }

                            val shapeStartPosition = shapeStartPositions.getOrNull(shapeIndex) ?: return@DraggableShape
                            val currentDragOffset = dragOffsets.getOrNull(shapeIndex) ?: Offset.Zero
                            val currentShape = gameState.availableShapes.getOrNull(shapeIndex)

                            if (boardSize.value > 0 && currentShape != null) {
                                val shapeCenterOffset = calculateShapeCenterOffset(currentShape)
                                val fingerOffset = Offset(0f, -200f)
                                val absolutePosition = shapeStartPosition + currentDragOffset

                                val shapeCenterX = absolutePosition.x - shapeCenterOffset.x + fingerOffset.x
                                val shapeCenterY = absolutePosition.y - shapeCenterOffset.y + fingerOffset.y

                                snapPreviewPosition = findSnapPosition(
                                    absoluteX = shapeCenterX,
                                    absoluteY = shapeCenterY,
                                    boardPosition = boardPosition,
                                    boardSize = boardSize.value,
                                    shape = currentShape
                                )
                            }
                        },
                        onDragEnd = { shapeIndex ->
                            val shapeStartPosition = shapeStartPositions.getOrNull(shapeIndex)
                            val currentDragOffset = dragOffsets.getOrNull(shapeIndex) ?: Offset.Zero
                            val currentShape = gameState.availableShapes.getOrNull(shapeIndex)

                            val finalPosition = if (snapPreviewPosition != null) {
                                snapPreviewPosition
                            } else if (boardSize.value > 0 && shapeStartPosition != null && currentShape != null) {
                                val shapeCenterOffset = calculateShapeCenterOffset(currentShape)
                                val fingerOffset = Offset(0f, -200f)
                                val absolutePosition = shapeStartPosition + currentDragOffset

                                val shapeCenterX = absolutePosition.x - shapeCenterOffset.x + fingerOffset.x
                                val shapeCenterY = absolutePosition.y - shapeCenterOffset.y + fingerOffset.y

                                findSnapPosition(
                                    absoluteX = shapeCenterX,
                                    absoluteY = shapeCenterY,
                                    boardPosition = boardPosition,
                                    boardSize = boardSize.value,
                                    shape = currentShape
                                )
                            } else {
                                null
                            }

                            if (finalPosition != null) {
                                gameViewModel.placeShape(shapeIndex, finalPosition)
                            }

                            draggingShapeIndex = null
                            dragOffsets = dragOffsets.mapIndexed { i, offset ->
                                if (i == shapeIndex) Offset.Zero else offset
                            }
                            snapPreviewPosition = null
                        },
                        onPositioned = { position ->
                            val newPositions = shapeStartPositions.toMutableList()
                            if (newPositions.size <= index) {
                                newPositions.add(index, position)
                            } else {
                                newPositions[index] = position
                            }
                            shapeStartPositions = newPositions
                        }
                    )
                }
            }
        }
    }
}


private fun findSnapPosition(
    absoluteX: Float,
    absoluteY: Float,
    boardPosition: Offset,
    boardSize: Float,
    shape: Shape
): Pair<Int, Int>? {
    val boardRight = boardPosition.x + 8 * boardSize
    val boardBottom = boardPosition.y + 8 * boardSize

    if (absoluteX < boardPosition.x - boardSize ||
        absoluteX > boardRight + boardSize ||
        absoluteY < boardPosition.y - boardSize ||
        absoluteY > boardBottom + boardSize) {
        return null
    }

    val shapeWidth = shape.blocks.maxOf { it.first } + 1
    val shapeHeight = shape.blocks.maxOf { it.second } + 1

    val relativeX = absoluteX - boardPosition.x
    val relativeY = absoluteY - boardPosition.y

    val boardX = (relativeX / boardSize).roundToInt()
    val boardY = (relativeY / boardSize).roundToInt()

    val clampedX = boardX.coerceIn(0, 8 - shapeWidth)
    val clampedY = boardY.coerceIn(0, 8 - shapeHeight)

    return Pair(clampedX, clampedY)
}

private fun calculateShapeCenterOffset(shape: Shape): Offset {
    val blocks = shape.blocks

    val minX = blocks.minOf { it.first }
    val maxX = blocks.maxOf { it.first }
    val minY = blocks.minOf { it.second }
    val maxY = blocks.maxOf { it.second }

    val centerX = (minX + maxX) / 2f
    val centerY = (minY + maxY) / 2f

    return Offset(centerX * 40f, centerY * 40f)
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

    val fingerOffset = remember { Offset(0f, -200f) }

    val dragAreaSize by remember(shape) {
        derivedStateOf {
            when {
                shape.type == ShapeType.LINE_1x4 -> 130.dp
                shape.blocks.maxOf { it.second } >= 3 -> 110.dp
                shape.blocks.maxOf { it.first } >= 3 -> 110.dp
                else -> 90.dp
            }
        }
    }

    Box(
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                onPositioned(coordinates.positionInRoot())
            }
    ) {
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        if (isDragging) (dragOffset.x - shapeCenterOffset.x + fingerOffset.x).roundToInt() else 0,
                        if (isDragging) (dragOffset.y - shapeCenterOffset.y + fingerOffset.y).roundToInt() else 0
                    )
                }
        ) {
            when (shape.type) {
                ShapeType.SQUARE -> SquareShape(shape.color)
                ShapeType.LINE_1x4 -> Line1x4Shape(shape.color)
                ShapeType.LINE_1x3 -> Line1x3Shape(shape.color)
                ShapeType.LINE_1x2 -> Line1x2Shape(shape.color)
                ShapeType.LINE_4x1 -> Line4x1Shape(shape.color)
                ShapeType.LINE_3x1 -> Line3x1Shape(shape.color)
                ShapeType.LINE_2x1 -> Line2x1Shape(shape.color)
                ShapeType.TRIANGLE_3 -> Triangle3Shape(shape.color)
            }
        }

        if (isActive) {
            Box(
                modifier = Modifier
                    .size(dragAreaSize)
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
        } else {
            Box(
                modifier = Modifier.size(dragAreaSize)
            )
        }
    }
}
@Composable
fun Line1x4Shape(color: BlockColor) {
    Column {
        BlockView(color)
        BlockView(color)
        BlockView(color)
        BlockView(color)
    }
}

@Composable
fun Line1x3Shape(color: BlockColor) {
    Column {
        BlockView(color)
        BlockView(color)
        BlockView(color)
    }
}

@Composable
fun Line1x2Shape(color: BlockColor) {
    Column {
        BlockView(color)
        BlockView(color)
    }
}

@Composable
fun Line4x1Shape(color: BlockColor) {
    Row {
        BlockView(color)
        BlockView(color)
        BlockView(color)
        BlockView(color)
    }
}

@Composable
fun Line3x1Shape(color: BlockColor) {
    Row {
        BlockView(color)
        BlockView(color)
        BlockView(color)
    }
}

@Composable
fun Line2x1Shape(color: BlockColor) {
    Row {
        BlockView(color)
        BlockView(color)
    }
}

@Composable
fun Triangle3Shape(color: BlockColor) {
    Column {
        Row {
            Box(modifier = Modifier.size(40.dp))
            BlockView(color)
        }
        Row {
            BlockView(color)
            BlockView(color)
        }
    }
}

@Composable
fun SquareShape(color: BlockColor) {
    Column {
        Row {
            BlockView(color)
            BlockView(color)
        }
        Row {
            BlockView(color)
            BlockView(color)
        }
    }
}

@Composable
fun BlockView(color: BlockColor) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(2.dp)
            .background(
                color = color.colorValue,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
            )
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
            )
    )
}

@Composable
fun GameBoard(
    board: List<List<Block>>,
    snapPreviewPosition: Pair<Int, Int>?,
    draggingShape: Shape?,
    modifier: Modifier = Modifier
) {
    val flattenedBlocks by remember(board) {
        derivedStateOf { board.flatten() }
    }

    val canPlaceHere by remember(snapPreviewPosition, board, draggingShape) {
        derivedStateOf {
            snapPreviewPosition?.let { position ->
                draggingShape?.let { shape ->
                    canPlaceShapeAtPosition(board, shape, position)
                } ?: false
            } ?: false
        }
    }

    Box(modifier = modifier) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(8),
            modifier = Modifier
        ) {
            items(
                items = flattenedBlocks,
                key = { block -> "block_${block.x}_${block.y}" }
            ) { block ->
                val isHighlighted by remember(snapPreviewPosition, block, draggingShape) {
                    derivedStateOf {
                        snapPreviewPosition?.let { (snapX, snapY) ->
                            draggingShape?.let { shape ->
                                shape.blocks.any { (dx, dy) ->
                                    block.x == snapX + dx && block.y == snapY + dy
                                }
                            } ?: false
                        } ?: false
                    }
                }

                BoardBlockView(
                    block = block,
                    isHighlighted = isHighlighted && canPlaceHere,
                    isValidPosition = canPlaceHere
                )
            }
        }
    }
}

private fun canPlaceShapeAtPosition(board: List<List<Block>>, shape: Shape, position: Pair<Int, Int>): Boolean {
    val (startX, startY) = position

    for (block in shape.blocks) {
        val (dx, dy) = block
        val x = startX + dx
        val y = startY + dy

        if (x < 0 || x >= 8 || y < 0 || y >= 8) {
            return false
        }

        if (board[y][x].color != null) {
            return false
        }
    }
    return true
}

@Composable
fun BoardBlockView(
    block: Block,
    isHighlighted: Boolean = false,
    isValidPosition: Boolean = true
) {
    val blockColor = block.color?.colorValue ?: Color.White

    val backgroundColor by remember(blockColor, isHighlighted, isValidPosition) {
        derivedStateOf {
            when {
                !isHighlighted -> blockColor
                isValidPosition && block.color == null -> Color(0x802196F3)
                isValidPosition && block.color != null -> {
                    Color(
                        red = (0x21 * 0.3 + blockColor.red * 255 * 0.7).toInt() / 255f,
                        green = (0x96 * 0.3 + blockColor.green * 255 * 0.7).toInt() / 255f,
                        blue = (0xF3 * 0.3 + blockColor.blue * 255 * 0.7).toInt() / 255f,
                        alpha = 1f
                    )
                }
                else -> {
                    Color(
                        red = (0xFF * 0.3 + blockColor.red * 255 * 0.7).toInt() / 255f,
                        green = (0x44 * 0.3 + blockColor.green * 255 * 0.7).toInt() / 255f,
                        blue = (0x44 * 0.3 + blockColor.blue * 255 * 0.7).toInt() / 255f,
                        alpha = 1f
                    )
                }
            }
        }
    }

    val borderColor by remember(isHighlighted, isValidPosition) {
        derivedStateOf {
            when {
                !isHighlighted -> Color.Gray
                isValidPosition -> Color(0xFF2196F3)
                else -> Color(0xFFFF4444)
            }
        }
    }

    val borderWidth = if (isHighlighted) 2.dp else 1.dp

    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(2.dp)
            .background(
                color = backgroundColor,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
            )
            .border(
                width = borderWidth,
                color = borderColor,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
            )
    )
}

@Composable
fun GameOverDialog(
    score: Int,
    highScore: Int,
    onRestart: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text("Игра окончена!") },
        text = {
            Column {
                Text("Ваш счет: $score")
                Text("Рекорд: $highScore")
            }
        },
        confirmButton = {
            Button(onClick = onRestart) {
                Text("Новая игра")
            }
        }
    )
}