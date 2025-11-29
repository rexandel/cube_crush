package com.rexandel.cube_crush.domain.repositories

interface ScoreRepository {
    fun getHighScore(): Int
    fun saveHighScore(score: Int)
    fun updateHighScore(newScore: Int): Int
}