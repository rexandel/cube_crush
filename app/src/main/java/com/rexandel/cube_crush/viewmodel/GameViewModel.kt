package com.rexandel.cube_crush.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rexandel.cube_crush.data.managers.StringResources
import com.rexandel.cube_crush.domain.game.GameModel
import com.rexandel.cube_crush.domain.repositories.UserRepository
import com.rexandel.cube_crush.domain.repositories.ScoreRepository
import com.rexandel.cube_crush.domain.entities.Shape
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import com.rexandel.cube_crush.data.managers.SoundManager

class GameViewModel(
    application: Application,
    private val userRepository: UserRepository,
    private val scoreRepository: ScoreRepository
) : AndroidViewModel(application) {

    private val gameModel = GameModel()
    private val gameStateManager = GameStateManager(gameModel)
    private val dragHandler = DragHandler(gameModel)
    private val uiEffectsManager = UiEffectsManager()
    private val soundManager = SoundManager.getInstance(application)

    private val _uiState = MutableStateFlow(
        GameUiState(
            gameState = gameStateManager.getCurrentState(),
            dragState = dragHandler.getCurrentDragState(),
            uiEffects = uiEffectsManager.getCurrentEffects(),
            linesToClear = emptySet()
        )
    )
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        refreshHighScore()
    }

    fun startDrag(shapeIndex: Int) {
        dragHandler.startDrag(shapeIndex)
        updateUiState()
    }

    fun updateDragPosition(snapPreviewPosition: Pair<Int, Int>?, canPlace: Boolean) {
        dragHandler.updateDragPosition(snapPreviewPosition, canPlace)

        val linesToClear = if (canPlace && snapPreviewPosition != null) {
            calculateLinesToClear(snapPreviewPosition)
        } else {
            emptySet()
        }

        _uiState.value = _uiState.value.copy(
            dragState = dragHandler.getCurrentDragState(),
            linesToClear = linesToClear
        )
    }

    fun placeShape(shapeIndex: Int, position: Pair<Int, Int>) {
        val result = gameStateManager.placeShape(shapeIndex, position)

        if (result is PlaceShapeResult.Success) {
            soundManager.playSound(SoundManager.Sound.BLOCK)
            uiEffectsManager.showScoreAnimation(
                linesCleared = result.linesCleared,
                scoreEarned = result.scoreEarned,
                comboCount = result.comboCount
            )

            if (result.linesCleared > 0) {
                soundManager.playSound(SoundManager.Sound.LINE_CLEARED)
            }

            val currentState = gameStateManager.getCurrentState()
            if (currentState.isGameOver) {
                soundManager.playSound(SoundManager.Sound.GAME_OVER)
                submitScore()
            }
        }

        _uiState.value = _uiState.value.copy(
            linesToClear = emptySet()
        )
        updateUiState()
    }

    fun endDrag() {
        dragHandler.endDrag()
        _uiState.value = _uiState.value.copy(
            dragState = dragHandler.getCurrentDragState(),
            linesToClear = emptySet()
        )
    }

    fun canPlaceShape(shapeIndex: Int, position: Pair<Int, Int>): Boolean {
        return gameStateManager.canPlaceShape(shapeIndex, position)
    }

    fun restartGame() {
        val currentState = gameStateManager.getCurrentState()
        if (!currentState.isGameOver && currentState.score > 0) {
            submitScore(currentState.score)
        }
        gameStateManager.restartGame()
        dragHandler.reset()
        uiEffectsManager.reset()
        refreshHighScore()
        updateUiState()
    }

    fun scoreAnimationCompleted() {
        uiEffectsManager.hideScoreAnimation()
        updateUiState()
    }

    fun shareScore() {
        val context = getApplication<Application>()
        val currentState = _uiState.value.gameState
        val score = currentState.score
        val highScore = currentState.highScore

        val shareText = if (score == highScore) {
            StringResources.shareTextNewRecord(context, score)
        } else {
            StringResources.shareTextRegular(context, score, highScore)
        }

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(intent, StringResources.shareDialogTitle(context))
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    private fun updateUiState() {
        _uiState.value = GameUiState(
            gameState = gameStateManager.getCurrentState(),
            dragState = dragHandler.getCurrentDragState(),
            uiEffects = uiEffectsManager.getCurrentEffects(),
            linesToClear = _uiState.value.linesToClear,
            error = _uiState.value.error
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun submitScore(score: Int? = null) {
        val scoreToSave = score ?: _uiState.value.gameState.score
        viewModelScope.launch {
            try {
                scoreRepository.submitScore(scoreToSave)
            } catch (e: java.io.IOException) {
                // Suppress connection errors in game screen as user is already aware
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message ?: StringResources.getUnknownError(getApplication()))
            }
        }
    }

    private fun refreshHighScore() {
        viewModelScope.launch {
            try {
                val savedHighScore = scoreRepository.getHighScore()
                gameStateManager.updateHighScore(savedHighScore)
                updateUiState()
            } catch (e: java.io.IOException) {
                // Suppress connection errors in game screen as user is already aware
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message ?: StringResources.getUnknownError(getApplication()))
            }
        }
    }

    private fun calculateLinesToClear(position: Pair<Int, Int>): Set<Int> {
        val dragState = dragHandler.getCurrentDragState()
        val shapeIndex = dragState.draggingShapeIndex ?: return emptySet()

        val shape = gameStateManager.getCurrentState().availableShapes.getOrNull(shapeIndex) ?: return emptySet()
        val (startX, startY) = position

        val tempBoard = createTempBoardWithShape(shape, position)

        val linesToClear = mutableSetOf<Int>()

        for (y in 0 until 8) {
            if (isLineFull(tempBoard[y])) {
                linesToClear.add(y)
            }
        }

        for (x in 0 until 8) {
            val column = List(8) { y -> tempBoard[y][x] }
            if (isLineFull(column)) {
                linesToClear.add(x + 8)
            }
        }

        return linesToClear
    }

    private fun createTempBoardWithShape(shape: Shape, position: Pair<Int, Int>): List<List<com.rexandel.cube_crush.domain.entities.Block>> {
        val currentBoard = gameStateManager.getCurrentState().board
        val tempBoard = currentBoard.map { row ->
            row.map { block ->
                block.copy()
            }.toMutableList()
        }.toMutableList()

        val (startX, startY) = position

        for (y in 0 until shape.height) {
            for (x in 0 until shape.width) {
                if (shape.matrix[y][x]) {
                    val boardX = startX + x
                    val boardY = startY + y

                    if (boardX in 0 until 8 && boardY in 0 until 8) {
                        tempBoard[boardY][boardX] = tempBoard[boardY][boardX].copy(color = shape.color)
                    }
                }
            }
        }

        return tempBoard
    }

    private fun isLineFull(line: List<com.rexandel.cube_crush.domain.entities.Block>): Boolean {
        return line.all { it.color != null }
    }
}