package com.rexandel.cube_crush.domain.game

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import com.rexandel.cube_crush.data.managers.StringResources
import com.rexandel.cube_crush.data.network.NetworkModule
import com.rexandel.cube_crush.domain.entities.GameState

data class LoadingState(
    val progress: Float = 0f,
    val currentStep: String = "",
    val isComplete: Boolean = false,
    val preloadedGameState: GameState? = null,
    val error: String? = null
)

class ResourceLoader(private val context: Context) {

    private val _loadingState = MutableStateFlow(LoadingState())
    val loadingState: StateFlow<LoadingState> = _loadingState

    suspend fun loadAllResources() {
        _loadingState.value = LoadingState(
            progress = 0f,
            currentStep = StringResources.getLoadingPreparing(context)
        )

        try {
            updateProgress(StringResources.getLoadingCreatingBoard(context), 0.2f)
            val gameState = preloadGameState()

            updateProgress(StringResources.getLoadingInitializingSystems(context), 0.5f)
            initializeGameComponents()

            updateProgress(StringResources.getLoadingCheckingServer(context), 0.8f)
            
            var connectionError: String? = null
            try {
                checkServerConnection()
            } catch (e: Exception) {
                connectionError = StringResources.getConnectionError(context)
            }

            updateProgress(StringResources.getLoadingComplete(context), 1.0f)

            _loadingState.value = LoadingState(
                progress = 1.0f,
                currentStep = StringResources.getLoadingReady(context),
                isComplete = true,
                preloadedGameState = gameState,
                error = connectionError
            )

        } catch (e: Exception) {
            _loadingState.value = LoadingState(
                progress = 0f,
                currentStep = StringResources.getLoadingError(context, e.message ?: ""),
                isComplete = false,
                error = e.message ?: StringResources.getUnknownError(context)
            )
        }
    }

    private suspend fun preloadGameState(): GameState = withContext(Dispatchers.Default) {
        val boardManager = BoardManager()
        val emptyBoard = boardManager.createEmptyBoard()

        val initialShapes = try {
            ShapeFactory.generateSmartShapes(emptyBoard, 3)
        } catch (e: Exception) {
            ShapeFactory.generateUniqueRandomShapes(3)
        }

        GameState(
            board = emptyBoard,
            availableShapes = initialShapes,
            score = 0,
            highScore = 0,
            isGameOver = false,
            comboCount = 0
        )
    }

    private suspend fun initializeGameComponents() = withContext(Dispatchers.Default) {
        ShapeFactory.generateUniqueRandomShapes(2)
        val testModel = com.rexandel.cube_crush.domain.game.GameModel()
        testModel.createNewGame()
        val boardManager = BoardManager()
        boardManager.createEmptyBoard()
    }

    private suspend fun checkServerConnection() = withContext(Dispatchers.IO) {
        try {
            val gameApi = NetworkModule.getGameApi(context)
            gameApi.getTopPlayers()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    private fun updateProgress(step: String, progress: Float) {
        _loadingState.value = _loadingState.value.copy(
            currentStep = step,
            progress = progress
        )

        try {
            Thread.sleep(100)
        } catch (e: InterruptedException) {
        }
    }

    fun getPreloadedGameState(): GameState? {
        return _loadingState.value.preloadedGameState
    }
}