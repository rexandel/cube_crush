package com.rexandel.cube_crush.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.rexandel.cube_crush.viewmodel.GameViewModel
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun GameScreen() {
    val gameViewModel: GameViewModel = viewModel()
    val gameState = gameViewModel.gameState

    var isDragging by remember { mutableStateOf(false) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var boardPosition by remember { mutableStateOf(Offset.Zero) }
    var boardSize by remember { mutableStateOf(0.dp) }
    var shapeStartPosition by remember { mutableStateOf(Offset.Zero) }
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
                    val newPosition = coordinates.positionInRoot()
                    val newSize = with(coordinates.size) { (width / 8).dp }

                    if (boardPosition == Offset.Zero ||
                        abs(boardPosition.x - newPosition.x) > 1f ||
                        abs(boardPosition.y - newPosition.y) > 1f) {
                        boardPosition = newPosition
                    }
                    if (boardSize != newSize) {
                        boardSize = newSize
                    }
                }
        ) {
            GameBoard(
                board = gameState.board,
                snapPreviewPosition = snapPreviewPosition,
                gameViewModel = gameViewModel,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .onGloballyPositioned { coordinates ->
                        val newPosition = coordinates.positionInRoot()
                        if (shapeStartPosition == Offset.Zero) {
                            shapeStartPosition = newPosition
                        }
                    }
            ) {
                if (isDragging) {
                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(dragOffset.x.roundToInt(), dragOffset.y.roundToInt())
                            }
                    ) {
                        SquareShape()
                    }
                } else {
                    SquareShape()
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(90.dp)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                isDragging = true
                                dragOffset = Offset.Zero
                                snapPreviewPosition = null
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                dragOffset += dragAmount

                                val absoluteX = shapeStartPosition.x + dragOffset.x
                                val absoluteY = shapeStartPosition.y + dragOffset.y

                                if (boardSize.value > 0) {
                                    val boardX = ((absoluteX - boardPosition.x) / boardSize.value).toInt()
                                    val boardY = ((absoluteY - boardPosition.y) / boardSize.value).toInt()

                                    snapPreviewPosition = findSnapPosition(boardX, boardY)
                                }
                            },
                            onDragEnd = {
                                val finalPosition = if (snapPreviewPosition != null) {
                                    snapPreviewPosition
                                } else if (boardSize.value > 0) {
                                    val absoluteX = shapeStartPosition.x + dragOffset.x
                                    val absoluteY = shapeStartPosition.y + dragOffset.y
                                    val boardX = ((absoluteX - boardPosition.x) / boardSize.value).toInt()
                                    val boardY = ((absoluteY - boardPosition.y) / boardSize.value).toInt()
                                    findSnapPosition(boardX, boardY)
                                } else {
                                    null
                                }

                                if (finalPosition != null) {
                                    val success = gameViewModel.placeSquare(finalPosition)
                                    // Не нужно обрабатывать success здесь
                                }

                                isDragging = false
                                dragOffset = Offset.Zero
                                snapPreviewPosition = null
                            },
                            onDragCancel = {
                                isDragging = false
                                dragOffset = Offset.Zero
                                snapPreviewPosition = null
                            }
                        )
                    }
            )
        }
    }
}

private fun findSnapPosition(boardX: Int, boardY: Int): Pair<Int, Int>? {
    val clampedX = boardX.coerceIn(0, 6)
    val clampedY = boardY.coerceIn(0, 6)
    return Pair(clampedX, clampedY)
}

@Composable
fun GameBoard(
    board: List<List<Block>>,
    snapPreviewPosition: Pair<Int, Int>?,
    gameViewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val flattenedBlocks by remember(board) {
        derivedStateOf { board.flatten() }
    }

    val canPlaceHere by remember(snapPreviewPosition, board) {
        derivedStateOf {
            snapPreviewPosition?.let { position ->
                canPlaceSquareAtPosition(board, position)
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
                val isHighlighted by remember(snapPreviewPosition, block) {
                    derivedStateOf {
                        snapPreviewPosition?.let { (snapX, snapY) ->
                            val relativeX = block.x - snapX
                            val relativeY = block.y - snapY
                            relativeX in 0..1 && relativeY in 0..1
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

private fun canPlaceSquareAtPosition(board: List<List<Block>>, position: Pair<Int, Int>): Boolean {
    val (startX, startY) = position

    if (startX < 0 || startX > 6 || startY < 0 || startY > 6) {
        return false
    }

    for (dy in 0..1) {
        for (dx in 0..1) {
            val x = startX + dx
            val y = startY + dy
            if (board[y][x].color != null) {
                return false
            }
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
fun SquareShape() {
    Column {
        Row {
            BlockView()
            BlockView()
        }
        Row {
            BlockView()
            BlockView()
        }
    }
}

@Composable
fun BlockView() {
    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(2.dp)
            .background(
                color = Color(0xFF2196F3),
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