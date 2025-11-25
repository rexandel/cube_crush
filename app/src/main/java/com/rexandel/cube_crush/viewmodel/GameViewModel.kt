package com.rexandel.cube_crush.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rexandel.cube_crush.domain.game.GameModel
import com.rexandel.cube_crush.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val gameModel = GameModel()
    private val gameStateManager = GameStateManager(gameModel)
    private val dragHandler = DragHandler(gameModel)
    private val uiEffectsManager = UiEffectsManager()

    private val _uiState = MutableStateFlow(
        GameUiState(
            gameState = gameStateManager.getCurrentState(),
            dragState = dragHandler.getCurrentDragState(),
            uiEffects = uiEffectsManager.getCurrentEffects()
        )
    )
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        updateHighScoreFromRepository()
    }

    fun startDrag(shapeIndex: Int) {
        dragHandler.startDrag(shapeIndex)
        updateUiState()
    }

    fun updateDragPosition(snapPreviewPosition: Pair<Int, Int>?, canPlace: Boolean) {
        dragHandler.updateDragPosition(snapPreviewPosition, canPlace)
        updateUiState()
    }

    fun placeShape(shapeIndex: Int, position: Pair<Int, Int>) {
        val result = gameStateManager.placeShape(shapeIndex, position)

        if (result is PlaceShapeResult.Success) {
            uiEffectsManager.showScoreAnimation(result.linesCleared)

            // Проверяем, нужно ли сохранять новый рекорд
            val currentState = gameStateManager.getCurrentState()
            if (currentState.score == currentState.highScore) {
                updateHighScore() // Сохраняем в репозиторий только если это новый рекорд
            }
        }

        updateUiState()
    }

    fun endDrag() {
        dragHandler.endDrag()
        updateUiState()
    }

    fun canPlaceShape(shapeIndex: Int, position: Pair<Int, Int>): Boolean {
        return gameStateManager.canPlaceShape(shapeIndex, position)
    }

    fun restartGame() {
        gameStateManager.restartGame()
        dragHandler.reset()
        uiEffectsManager.reset()
        updateHighScoreFromRepository()
        updateUiState()
    }

    fun scoreAnimationCompleted() {
        uiEffectsManager.hideScoreAnimation()
        updateUiState()
    }

    private fun updateUiState() {
        _uiState.value = GameUiState(
            gameState = gameStateManager.getCurrentState(),
            dragState = dragHandler.getCurrentDragState(),
            uiEffects = uiEffectsManager.getCurrentEffects()
        )
    }

    private fun updateHighScore() {
        viewModelScope.launch {
            val currentScore = _uiState.value.gameState.score
            userRepository.updateHighScore(currentScore)
        }
    }

    private fun updateHighScoreFromRepository() {
        viewModelScope.launch {
            val savedHighScore = userRepository.getHighScore()
            gameStateManager.updateHighScore(savedHighScore)
            updateUiState()
        }
    }
}