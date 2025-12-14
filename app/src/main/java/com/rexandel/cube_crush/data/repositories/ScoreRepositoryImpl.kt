package com.rexandel.cube_crush.data.repositories

import android.content.Context
import com.rexandel.cube_crush.data.database.AppDatabase
import com.rexandel.cube_crush.data.database.entities.ScoreEntity
import com.rexandel.cube_crush.domain.entities.PlayerScore
import com.rexandel.cube_crush.domain.entities.Score
import com.rexandel.cube_crush.domain.entities.UserStats
import com.rexandel.cube_crush.domain.repositories.ScoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ScoreRepositoryImpl private constructor(context: Context) : ScoreRepository {
    private val db = AppDatabase.getDatabase(context)
    private val scoreDao = db.scoreDao()
    private val userDao = db.userDao()
    private val userRepository = UserRepositoryImpl.getInstance(context)

    companion object {
        @Volatile
        private var INSTANCE: ScoreRepositoryImpl? = null

        fun getInstance(context: Context): ScoreRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ScoreRepositoryImpl(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    private suspend fun getCurrentUserId(): Long? {
        val email = userRepository.getCurrentUser() ?: return null
        return userDao.findByEmail(email)?.id
    }

    override suspend fun getHighScore(): Int = withContext(Dispatchers.IO) {
        val userId = getCurrentUserId() ?: return@withContext 0
        scoreDao.getBestScore(userId) ?: 0
    }

    override suspend fun saveScore(score: Int) = withContext(Dispatchers.IO) {
        val userId = getCurrentUserId() ?: return@withContext
        val scoreEntity = ScoreEntity(userId = userId, score = score)
        scoreDao.insert(scoreEntity)
        Unit
    }

    override suspend fun submitScore(newScore: Int): Int = withContext(Dispatchers.IO) {
        val currentHighScore = getHighScore()

        saveScore(newScore)
        
        return@withContext if (newScore > currentHighScore) newScore else currentHighScore
    }

    override suspend fun getHistory(): List<Score> = withContext(Dispatchers.IO) {
        val userId = getCurrentUserId() ?: return@withContext emptyList()
        scoreDao.getHistory(userId).map { 
            Score(score = it.score, date = it.achievedAt) 
        }
    }

    override suspend fun getTopPlayers(): List<PlayerScore> = withContext(Dispatchers.IO) {
        scoreDao.getTopPlayers().map {
            PlayerScore(nickname = it.nickname, score = it.score)
        }
    }

    override suspend fun getUserStats(): UserStats? = withContext(Dispatchers.IO) {
        val userId = getCurrentUserId() ?: return@withContext null
        scoreDao.getUserStats(userId)?.let {
            UserStats(
                gamesPlayed = it.gamesPlayed,
                bestScore = it.bestScore ?: 0,
                averageScore = it.averageScore ?: 0
            )
        }
    }
}