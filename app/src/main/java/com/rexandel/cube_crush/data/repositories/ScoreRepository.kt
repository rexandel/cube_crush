package com.rexandel.cube_crush.data.repositories

import android.content.Context
import android.content.SharedPreferences

class ScoreRepository private constructor(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences("game_data", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var INSTANCE: ScoreRepository? = null

        fun getInstance(context: Context): ScoreRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ScoreRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    fun getHighScore(): Int {
        return sharedPref.getInt("high_score", 0)
    }

    fun saveHighScore(score: Int) {
        sharedPref.edit().putInt("high_score", score).apply()
    }

    fun updateHighScore(newScore: Int): Int {
        val currentHighScore = getHighScore()
        if (newScore > currentHighScore) {
            saveHighScore(newScore)
            return newScore
        }
        return currentHighScore
    }
}