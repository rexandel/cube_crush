package com.rexandel.cube_crush.data.repositories

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.rexandel.cube_crush.data.network.NetworkModule
import com.rexandel.cube_crush.data.network.dto.ScoreRequest
import com.rexandel.cube_crush.domain.entities.PlayerScore
import com.rexandel.cube_crush.domain.entities.Score
import com.rexandel.cube_crush.domain.entities.UserStats
import com.rexandel.cube_crush.domain.repositories.ScoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.format.DateTimeParseException

class ScoreRepositoryImpl private constructor(context: Context) : ScoreRepository {
    private val gameApi = NetworkModule.getGameApi(context)
    private val TAG = "ScoreRepository"

    companion object {
        @Volatile
        private var INSTANCE: ScoreRepositoryImpl? = null

        fun getInstance(context: Context): ScoreRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ScoreRepositoryImpl(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    override suspend fun getHighScore(): Int = withContext(Dispatchers.IO) {
        Log.d(TAG, "getHighScore")
        try {
            val score = gameApi.getUserStats().bestScore
            Log.d(TAG, "getHighScore: $score")
            score
        } catch (e: Exception) {
            Log.e(TAG, "getHighScore: error", e)
            0
        }
    }

    override suspend fun saveScore(score: Int) = withContext(Dispatchers.IO) {
        Log.d(TAG, "saveScore: $score")
        try {
            gameApi.submitScore(ScoreRequest(score))
            Log.d(TAG, "saveScore: success")
            Unit
        } catch (e: Exception) {
            Log.e(TAG, "saveScore: error", e)
            e.printStackTrace()
        }
    }

    override suspend fun submitScore(newScore: Int): Int = withContext(Dispatchers.IO) {
        Log.d(TAG, "submitScore: $newScore")
        try {
            gameApi.submitScore(ScoreRequest(newScore))
            val stats = gameApi.getUserStats()
            Log.d(TAG, "submitScore: success, new best=${stats.bestScore}")
            stats.bestScore
        } catch (e: Exception) {
            Log.e(TAG, "submitScore: error", e)
            e.printStackTrace()
            newScore
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getHistory(): List<Score> = withContext(Dispatchers.IO) {
        Log.d(TAG, "getHistory")
        try {
            val history = gameApi.getHistory().map { 
                Score(score = it.score, date = parseDate(it.achievedAt)) 
            }
            Log.d(TAG, "getHistory: success, count=${history.size}")
            history
        } catch (e: Exception) {
            Log.e(TAG, "getHistory: error", e)
            emptyList()
        }
    }

    override suspend fun getTopPlayers(): List<PlayerScore> = withContext(Dispatchers.IO) {
        Log.d(TAG, "getTopPlayers")
        try {
            val top = gameApi.getTopPlayers().map {
                PlayerScore(nickname = it.nickname, score = it.score)
            }
            Log.d(TAG, "getTopPlayers: success, count=${top.size}")
            top
        } catch (e: Exception) {
            Log.e(TAG, "getTopPlayers: error", e)
            emptyList()
        }
    }

    override suspend fun getUserStats(): UserStats? = withContext(Dispatchers.IO) {
        Log.d(TAG, "getUserStats")
        try {
            val stats = gameApi.getUserStats()
            Log.d(TAG, "getUserStats: success")
            UserStats(
                gamesPlayed = stats.gamesPlayed.toInt(),
                bestScore = stats.bestScore,
                averageScore = stats.averageScore.toInt()
            )
        } catch (e: Exception) {
            Log.e(TAG, "getUserStats: error", e)
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseDate(dateString: String): Long {
        return try {
            Instant.parse(dateString).toEpochMilli()
        } catch (e: DateTimeParseException) {
            try {
                java.time.LocalDateTime.parse(dateString).toInstant(java.time.ZoneOffset.UTC).toEpochMilli()
            } catch (e2: Exception) {
                System.currentTimeMillis()
            }
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
}
