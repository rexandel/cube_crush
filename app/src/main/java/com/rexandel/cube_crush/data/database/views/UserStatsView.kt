package com.rexandel.cube_crush.data.database.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView(
    viewName = "user_stats",
    value = """
        SELECT 
            u.id,
            u.nickname,
            u.created_at,
            COUNT(s.id) as games_played,
            MAX(s.score) as best_score,
            CAST(ROUND(AVG(s.score)) AS INTEGER) as average_score
        FROM users u
        LEFT JOIN scores s ON u.id = s.user_id
        GROUP BY u.id, u.nickname, u.created_at
    """
)
data class UserStatsView(
    val id: Long,
    val nickname: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "games_played")
    val gamesPlayed: Int,
    @ColumnInfo(name = "best_score")
    val bestScore: Int?,
    @ColumnInfo(name = "average_score")
    val averageScore: Int?
)
