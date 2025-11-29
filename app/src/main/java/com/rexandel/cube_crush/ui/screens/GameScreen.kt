package com.rexandel.cube_crush.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rexandel.cube_crush.data.repositories.ScoreRepositoryImpl
import com.rexandel.cube_crush.data.repositories.UserRepositoryImpl
import com.rexandel.cube_crush.ui.components.game.DragUtils
import com.rexandel.cube_crush.ui.components.game.GameBoard
import com.rexandel.cube_crush.ui.components.game.GameHeader
import com.rexandel.cube_crush.ui.components.game.GameOverDialog
import com.rexandel.cube_crush.ui.components.game.PauseDialog
import com.rexandel.cube_crush.ui.components.game.ShapesPanel
import com.rexandel.cube_crush.viewmodel.GameViewModel
import com.rexandel.cube_crush.viewmodel.GameViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun GameScreen(
    onExitToMenu: () -> Unit
) {
    val context = LocalContext.current
    val gameViewModel: GameViewModel = viewModel(
        factory = GameViewModelFactory(
            userRepository = UserRepositoryImpl.getInstance(context),
            scoreRepository = ScoreRepositoryImpl.getInstance(context)
        )
    )

    val uiState by gameViewModel.uiState.collectAsState()
    val gameState = uiState.gameState
    val dragState = uiState.dragState
    val uiEffects = uiState.uiEffects

    var isPaused by remember { mutableStateOf(false) }
    var boardPosition by remember { mutableStateOf(Offset.Zero) }
    var boardSize by remember { mutableStateOf(0.dp) }
    var shapeStartPositions by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var dragOffsets by remember { mutableStateOf(List(3) { Offset.Zero }) }

    val animatedScore = remember { Animatable(gameState.score.toFloat()) }
    val animatedHighScore = remember { Animatable(gameState.highScore.toFloat()) }

    LaunchedEffect(gameState.score) {
        if (gameState.score != animatedScore.value.toInt()) {
            launch {
                animatedScore.animateTo(
                    targetValue = gameState.score.toFloat(),
                    animationSpec = tween(durationMillis = 1000)
                )
            }
        }
    }

    LaunchedEffect(gameState.highScore) {
        if (gameState.highScore != animatedHighScore.value.toInt()) {
            launch {
                animatedHighScore.animateTo(
                    targetValue = gameState.highScore.toFloat(),
                    animationSpec = tween(durationMillis = 1000)
                )
            }
        }
    }

    LaunchedEffect(uiEffects.shouldAnimateScore) {
        if (uiEffects.shouldAnimateScore) {
            gameViewModel.scoreAnimationCompleted()
        }
    }

    val handleExitToMenu: () -> Unit = {
        gameViewModel.restartGame()
        onExitToMenu()
    }

    if (gameState.isGameOver) {
        GameOverDialog(
            score = gameState.score,
            highScore = gameState.highScore,
            onRestart = { gameViewModel.restartGame() },
            onExit = handleExitToMenu
        )
    }

    if (isPaused) {
        PauseDialog(
            onResume = { isPaused = false },
            onRestart = {
                isPaused = false
                gameViewModel.restartGame()
            },
            onExit = {
                isPaused = false
                handleExitToMenu()
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        GameHeader(
            highScore = animatedHighScore.value.toInt(),
            score = animatedScore.value.toInt(),
            onPauseClick = { isPaused = true }
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .onGloballyPositioned { coordinates ->
                    boardPosition = coordinates.positionInRoot()
                    boardSize = with(coordinates.size) { (width / 8).dp }
                }
        ) {
            GameBoard(
                board = uiState.gameState.board,
                snapPreviewPosition = uiState.dragState.snapPreviewPosition,
                draggingShape = uiState.dragState.draggingShapeIndex?.let { index ->
                    uiState.gameState.availableShapes.getOrNull(index)
                },
                canPlaceHere = uiState.dragState.canPlaceAtPreview,
                linesToClear = uiState.linesToClear,
                isHorizontalLine = { lineIndex -> uiState.isHorizontalLine(lineIndex) },
                modifier = Modifier.fillMaxSize()
            )
        }

        ShapesPanel(
            availableShapes = gameState.availableShapes,
            draggingShapeIndex = dragState.draggingShapeIndex,
            dragOffsets = dragOffsets,
            shapeStartPositions = shapeStartPositions,
            onDragStart = { shapeIndex ->
                gameViewModel.startDrag(shapeIndex)
                dragOffsets = dragOffsets.mapIndexed { i, offset ->
                    if (i == shapeIndex) Offset.Zero else offset
                }
            },
            onDrag = { shapeIndex, dragAmount ->
                dragOffsets = dragOffsets.mapIndexed { i, currentOffset ->
                    if (i == shapeIndex) currentOffset + dragAmount else currentOffset
                }

                val shapeStartPosition = shapeStartPositions.getOrNull(shapeIndex) ?: return@ShapesPanel
                val currentDragOffset = dragOffsets.getOrNull(shapeIndex) ?: Offset.Zero
                val currentShape = gameState.availableShapes.getOrNull(shapeIndex)

                if (boardSize.value > 0 && currentShape != null) {
                    val shapeCenterOffset = DragUtils.calculateShapeCenterOffset(currentShape)
                    val absolutePosition = DragUtils.calculateAbsoluteShapePosition(
                        shapeStartPosition = shapeStartPosition,
                        dragOffset = currentDragOffset,
                        shapeCenterOffset = shapeCenterOffset
                    )

                    val snapPosition = DragUtils.findSnapPosition(
                        absoluteX = absolutePosition.x,
                        absoluteY = absolutePosition.y,
                        boardPosition = boardPosition,
                        boardSize = boardSize.value,
                        shape = currentShape
                    )

                    val canPlace = snapPosition?.let { position ->
                        gameViewModel.canPlaceShape(shapeIndex, position)
                    } ?: false

                    gameViewModel.updateDragPosition(snapPosition, canPlace)
                }
            },
            onDragEnd = { shapeIndex ->
                val shapeStartPosition = shapeStartPositions.getOrNull(shapeIndex)
                val currentDragOffset = dragOffsets.getOrNull(shapeIndex) ?: Offset.Zero
                val currentShape = gameState.availableShapes.getOrNull(shapeIndex)

                val finalPosition = dragState.snapPreviewPosition ?: run {
                    if (boardSize.value > 0 && shapeStartPosition != null && currentShape != null) {
                        val shapeCenterOffset = DragUtils.calculateShapeCenterOffset(currentShape)
                        val absolutePosition = DragUtils.calculateAbsoluteShapePosition(
                            shapeStartPosition = shapeStartPosition,
                            dragOffset = currentDragOffset,
                            shapeCenterOffset = shapeCenterOffset
                        )

                        DragUtils.findSnapPosition(
                            absoluteX = absolutePosition.x,
                            absoluteY = absolutePosition.y,
                            boardPosition = boardPosition,
                            boardSize = boardSize.value,
                            shape = currentShape
                        )
                    } else {
                        null
                    }
                }

                if (finalPosition != null && currentShape != null &&
                    gameViewModel.canPlaceShape(shapeIndex, finalPosition)) {
                    gameViewModel.placeShape(shapeIndex, finalPosition)
                }

                gameViewModel.endDrag()
                dragOffsets = dragOffsets.mapIndexed { i, offset ->
                    if (i == shapeIndex) Offset.Zero else offset
                }
            },
            onShapePositioned = { index, position ->
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