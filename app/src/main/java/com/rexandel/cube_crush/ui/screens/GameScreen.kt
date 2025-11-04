package com.rexandel.cube_crush.ui.screens

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class GameScreen(context: Context) : View(context) {
    private val gridWidth = 8
    private val gridHeight = 8
    private val cellSize = 120f
    private var score = 1500
    private val record = 248000

    private val gridPaint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

    private val backgroundPaint = Paint().apply {
        color = Color.LTGRAY
        style = Paint.Style.FILL
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 60f
    }

    private val buttonPaint = Paint().apply {
        color = Color.GRAY
    }

    private val buttonTextPaint = Paint().apply {
        color = Color.WHITE
        textSize = 40f
    }

    private val scorePaint = Paint().apply {
        color = Color.BLACK
        textSize = 96f
        textAlign = Paint.Align.CENTER
    }

    private fun drawPauseButton(canvas: Canvas) {
        val buttonWidth = 200f
        val buttonHeight = 100f
        val buttonX = width - buttonWidth - 50f
        val buttonY = 50f

        canvas.drawRoundRect(
            buttonX,
            buttonY,
            buttonX + buttonWidth,
            buttonY + buttonHeight,
            20f,
            20f,
            buttonPaint
        )

        canvas.drawText("Пауза", buttonX + 40f, buttonY + 60f, buttonTextPaint)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)

        canvas.drawText("Рекорд: $record", 50f, 80f, textPaint)

        drawPauseButton(canvas)

        val startX = (width - gridWidth * cellSize) / 2
        val startY = (height - gridHeight * cellSize) / 2

        val scoreX = startX + (gridWidth * cellSize) / 2
        val scoreY = startY - 50f

        canvas.drawText(score.toString(), scoreX, scoreY, scorePaint)

        canvas.drawRect(startX, startY, startX + gridWidth * cellSize, startY + gridHeight * cellSize, backgroundPaint)

        for (row in 0 until gridHeight) {
            for (col in 0 until gridWidth) {
                val left = startX + col * cellSize
                val top = startY + row * cellSize
                canvas.drawRect(left, top, left + cellSize, top + cellSize, gridPaint)
            }
        }
    }
}