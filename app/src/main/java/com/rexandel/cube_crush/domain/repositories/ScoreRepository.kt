package com.rexandel.cube_crush.domain.repositories

import com.rexandel.cube_crush.domain.entities.PlayerScore
import com.rexandel.cube_crush.domain.entities.Score
import com.rexandel.cube_crush.domain.entities.UserStats

interface ScoreRepository {
    suspend fun getHighScore(): Int
    suspend fun saveScore(score: Int)
    suspend fun submitScore(newScore: Int): Int
    suspend fun getHistory(): List<Score>
    suspend fun getTopPlayers(): List<PlayerScore>
    suspend fun getUserStats(): UserStats?
}