package com.rexandel.cube_crush.viewmodel

import com.rexandel.cube_crush.domain.game.GameModel

class DragHandler(private val gameModel: GameModel) {

    private var _dragState = DragState()

    fun startDrag(shapeIndex: Int) {
        _dragState = DragState(
            draggingShapeIndex = shapeIndex,
            snapPreviewPosition = null,
            canPlaceAtPreview = false
        )
    }

    fun updateDragPosition(snapPreviewPosition: Pair<Int, Int>?, canPlace: Boolean) {
        _dragState = _dragState.copy(
            snapPreviewPosition = snapPreviewPosition,
            canPlaceAtPreview = canPlace
        )
    }

    fun endDrag() {
        _dragState = DragState()
    }

    fun reset() {
        _dragState = DragState()
    }

    fun getCurrentDragState(): DragState {
        return _dragState
    }

    fun getDraggingShape(): Int? {
        return _dragState.draggingShapeIndex
    }

    fun getPreviewPosition(): Pair<Int, Int>? {
        return _dragState.snapPreviewPosition
    }

    fun canPlaceAtCurrentPreview(): Boolean {
        return _dragState.canPlaceAtPreview
    }
}