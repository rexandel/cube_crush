package com.rexandel.cube_crush.viewmodel

class UiEffectsManager {

    private var _uiEffects = UiEffects()

    fun showScoreAnimation(linesCleared: Int) {
        _uiEffects = UiEffects(
            shouldAnimateScore = true,
            linesCleared = linesCleared
        )
    }

    fun hideScoreAnimation() {
        _uiEffects = _uiEffects.copy(
            shouldAnimateScore = false
        )
    }

    fun reset() {
        _uiEffects = UiEffects()
    }

    fun getCurrentEffects(): UiEffects {
        return _uiEffects
    }

    fun shouldAnimateScore(): Boolean {
        return _uiEffects.shouldAnimateScore
    }

    fun getLinesCleared(): Int {
        return _uiEffects.linesCleared
    }
}