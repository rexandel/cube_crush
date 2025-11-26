package com.rexandel.cube_crush.ui.components.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rexandel.cube_crush.R
import com.rexandel.cube_crush.domain.entities.Block
import com.rexandel.cube_crush.domain.entities.BlockColor
import com.rexandel.cube_crush.domain.entities.Shape

@Composable
fun GameBoard(
    board: List<List<Block>>,
    snapPreviewPosition: Pair<Int, Int>?,
    draggingShape: Shape?,
    canPlaceHere: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(8),
            modifier = Modifier
        ) {
            items(
                items = List(64) { index -> index },
                key = { index -> "cell_$index" }
            ) { index ->
                val x = index % 8
                val y = index / 8
                val block = board[y][x]

                val isHighlighted by remember(snapPreviewPosition, draggingShape, x, y, canPlaceHere) {
                    derivedStateOf {
                        if (!canPlaceHere) false else {
                            snapPreviewPosition?.let { (snapX, snapY) ->
                                draggingShape?.let { shape ->
                                    shape.matrix.forEachIndexed { dy, row ->
                                        row.forEachIndexed { dx, hasBlock ->
                                            if (hasBlock && x == snapX + dx && y == snapY + dy) {
                                                return@derivedStateOf true
                                            }
                                        }
                                    }
                                    false
                                } ?: false
                            } ?: false
                        }
                    }
                }

                BoardBlockView(
                    block = block,
                    isHighlighted = isHighlighted,
                    isValidPosition = canPlaceHere
                )
            }
        }
    }
}

@Composable
fun BoardBlockView(
    block: Block,
    isHighlighted: Boolean = false,
    isValidPosition: Boolean = true
) {
    val colorScheme = MaterialTheme.colorScheme

    val drawableResId = block.color?.let { color ->
        when (color) {
            BlockColor.YELLOW -> R.drawable.block_yellow
            BlockColor.RED -> R.drawable.block_red
            BlockColor.BLUE -> R.drawable.block_blue
            BlockColor.GREEN -> R.drawable.block_green
            BlockColor.PURPLE -> R.drawable.block_purple
        }
    }

    val borderColor by remember(isHighlighted, isValidPosition) {
        derivedStateOf {
            when {
                !isHighlighted -> Color.Gray
                isValidPosition -> colorScheme.tertiary
                else -> colorScheme.error
            }
        }
    }

    val borderWidth = if (isHighlighted) 2.dp else 1.dp

    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(2.dp)
            .border(
                width = borderWidth,
                color = borderColor
            )
    ) {
        if (drawableResId != null) {
            Image(
                painter = painterResource(id = drawableResId),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = colorScheme.surface
                    )
            )
        }
    }
}