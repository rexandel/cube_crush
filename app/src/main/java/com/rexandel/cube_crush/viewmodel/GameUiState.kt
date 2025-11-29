package com.rexandel.cube_crush.viewmodel

import com.rexandel.cube_crush.domain.entities.GameState

data class GameUiState(
    val gameState: GameState,
    val dragState: DragState,
    val uiEffects: UiEffects,
    val linesToClear: Set<Int> = emptySet()
) {
    fun isHorizontalLine(lineIndex: Int): Boolean {
        return lineIndex < 8
    }
}

data class DragState(
    val draggingShapeIndex: Int? = null,
    val snapPreviewPosition: Pair<Int, Int>? = null,
    val canPlaceAtPreview: Boolean = false
) {
    val isDragging: Boolean
        get() = draggingShapeIndex != null

    val hasPreview: Boolean
        get() = snapPreviewPosition != null
}

data class UiEffects(
    val shouldAnimateScore: Boolean = false,
    val linesCleared: Int = 0,
    val comboCount: Int = 0,
    val scoreEarned: Int = 0
) {
    val shouldShowLineClearEffect: Boolean
        get() = linesCleared > 0

    val shouldShowComboEffect: Boolean
        get() = comboCount > 0
}

sealed class PlaceShapeResult {
    data class Success(
        val linesCleared: Int,
        val scoreEarned: Int = linesCleared * 100,
        val comboCount: Int = 0
    ) : PlaceShapeResult()

    data class Failure(val message: String) : PlaceShapeResult()
}