package com.rexandel.cube_crush.ui.components.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.rexandel.cube_crush.domain.entities.Shape

@Composable
fun ShapesPanel(
    availableShapes: List<Shape>,
    draggingShapeIndex: Int?,
    dragOffsets: List<Offset>,
    shapeStartPositions: List<Offset>,
    onDragStart: (Int) -> Unit,
    onDrag: (Int, Offset) -> Unit,
    onDragEnd: (Int) -> Unit,
    onShapePositioned: (Int, Offset) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.1f))
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            availableShapes.forEachIndexed { index, shape ->
                key("${shape.type}_${shape.blocks.hashCode()}_$index") {
                    DraggableShape(
                        shape = shape,
                        shapeIndex = index,
                        isDragging = draggingShapeIndex == index,
                        isActive = draggingShapeIndex == null || draggingShapeIndex == index,
                        dragOffset = dragOffsets.getOrNull(index) ?: Offset.Zero,
                        onDragStart = onDragStart,
                        onDrag = onDrag,
                        onDragEnd = onDragEnd,
                        onPositioned = { position -> onShapePositioned(index, position) }
                    )
                }
            }
        }
    }
}