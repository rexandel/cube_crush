package com.rexandel.cube_crush.domain.repositories

interface ScoreRepository {
    suspend fun getHighScore(): Int
    suspend fun saveScore(score: Int)
    suspend fun submitScore(newScore: Int): Int
}