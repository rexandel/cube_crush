package com.rexandel.cube_crush.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rexandel.cube_crush.model.GameModel
import com.rexandel.cube_crush.model.GameState
import com.rexandel.cube_crush.model.Shape
import com.rexandel.cube_crush.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

class GameViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val gameModel = GameModel()

    private val _uiState = MutableStateFlow(
        GameUiState(
            gameState = gameModel.getCurrentState(),
            dragState = DragState(),
            uiEffects = UiEffects()
        )
    )
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        updateHighScoreFromRepository()
    }

    fun startDrag(shapeIndex: Int) {
        _uiState.value = _uiState.value.copy(
            dragState = _uiState.value.dragState.copy(
                draggingShapeIndex = shapeIndex,
                snapPreviewPosition = null,
                canPlaceAtPreview = false
            )
        )
    }

    fun updateDragPosition(snapPreviewPosition: Pair<Int, Int>?, canPlace: Boolean) {
        _uiState.value = _uiState.value.copy(
            dragState = _uiState.value.dragState.copy(
                snapPreviewPosition = snapPreviewPosition,
                canPlaceAtPreview = canPlace
            )
        )
    }

    fun placeShape(shapeIndex: Int, position: Pair<Int, Int>) {
        val result = gameModel.placeShape(shapeIndex, position)

        when (result) {
            is com.rexandel.cube_crush.model.PlaceShapeResult.Success -> {
                updateUiStateFromModel()
                updateHighScore()

                _uiState.value = _uiState.value.copy(
                    uiEffects = _uiState.value.uiEffects.copy(
                        shouldAnimateScore = true,
                        linesCleared = result.linesCleared
                    )
                )
            }
            is com.rexandel.cube_crush.model.PlaceShapeResult.Failure -> {
                Log.e("GameViewModel", "Failed to place shape: ${result.message}. Shape: $shapeIndex, Position: $position")
            }
        }
    }

    fun endDrag() {
        _uiState.value = _uiState.value.copy(
            dragState = DragState()
        )
    }

    fun canPlaceShape(shape: Shape, position: Pair<Int, Int>): Boolean {
        return gameModel.canPlaceShape(shape, position)
    }

    fun restartGame() {
        gameModel.restartGame()
        updateUiStateFromModel()
        updateHighScoreFromRepository()
        _uiState.value = _uiState.value.copy(
            dragState = DragState(),
            uiEffects = UiEffects()
        )
    }

    fun scoreAnimationCompleted() {
        _uiState.value = _uiState.value.copy(
            uiEffects = _uiState.value.uiEffects.copy(
                shouldAnimateScore = false
            )
        )
    }

    private fun updateUiStateFromModel() {
        _uiState.value = _uiState.value.copy(
            gameState = gameModel.getCurrentState()
        )
    }

    private fun updateHighScore() {
        viewModelScope.launch {
            val newHighScore = userRepository.updateHighScore(_uiState.value.gameState.score)
            if (newHighScore > _uiState.value.gameState.highScore) {
                gameModel.updateHighScore(newHighScore)
                updateUiStateFromModel()
            }
        }
    }

    private fun updateHighScoreFromRepository() {
        viewModelScope.launch {
            val savedHighScore = userRepository.getHighScore()
            if (savedHighScore > _uiState.value.gameState.highScore) {
                gameModel.updateHighScore(savedHighScore)
                updateUiStateFromModel()
            }
        }
    }
}

data class GameUiState(
    val gameState: GameState,
    val dragState: DragState,
    val uiEffects: UiEffects
)

data class DragState(
    val draggingShapeIndex: Int? = null,
    val snapPreviewPosition: Pair<Int, Int>? = null,
    val canPlaceAtPreview: Boolean = false
)

data class UiEffects(
    val shouldAnimateScore: Boolean = false,
    val linesCleared: Int = 0
)