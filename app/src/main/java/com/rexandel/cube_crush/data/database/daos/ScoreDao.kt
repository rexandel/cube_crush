package com.rexandel.cube_crush.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rexandel.cube_crush.data.database.entities.ScoreEntity
import com.rexandel.cube_crush.data.database.views.TopPlayerView
import com.rexandel.cube_crush.data.database.views.UserStatsView

@Dao
interface ScoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(score: ScoreEntity): Long

    @Query("SELECT * FROM scores WHERE user_id = :userId ORDER BY achieved_at DESC")
    suspend fun getHistory(userId: Long): List<ScoreEntity>

    @Query("SELECT MAX(score) FROM scores WHERE user_id = :userId")
    suspend fun getBestScore(userId: Long): Int?

    @Query("SELECT * FROM top_players")
    suspend fun getTopPlayers(): List<TopPlayerView>

    @Query("SELECT * FROM user_stats WHERE id = :userId")
    suspend fun getUserStats(userId: Long): UserStatsView?
}
