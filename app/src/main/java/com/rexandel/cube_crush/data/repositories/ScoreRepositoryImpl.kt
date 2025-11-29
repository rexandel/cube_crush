package com.rexandel.cube_crush.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.rexandel.cube_crush.domain.repositories.ScoreRepository

class ScoreRepositoryImpl private constructor(context: Context) : ScoreRepository {
    private val sharedPref: SharedPreferences = context.getSharedPreferences("game_data", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var INSTANCE: ScoreRepositoryImpl? = null

        fun getInstance(context: Context): ScoreRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ScoreRepositoryImpl(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    override fun getHighScore(): Int {
        return sharedPref.getInt("high_score", 0)
    }

    override fun saveHighScore(score: Int) {
        sharedPref.edit().putInt("high_score", score).apply()
    }

    override fun updateHighScore(newScore: Int): Int {
        val currentHighScore = getHighScore()
        if (newScore > currentHighScore) {
            saveHighScore(newScore)
            return newScore
        }
        return currentHighScore
    }
}