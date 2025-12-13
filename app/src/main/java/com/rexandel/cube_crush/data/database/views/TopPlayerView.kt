package com.rexandel.cube_crush.data.database.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView(
    viewName = "top_players",
    value = """
        SELECT 
            u.id,
            u.nickname,
            MAX(s.score) as score,
            MAX(s.achieved_at) as achieved_at
        FROM scores s
        JOIN users u ON u.id = s.user_id
        GROUP BY u.id, u.nickname
        ORDER BY score DESC
        LIMIT 10
    """
)
data class TopPlayerView(
    val id: Long,
    val nickname: String,
    val score: Int,
    @ColumnInfo(name = "achieved_at")
    val achievedAt: Long
)
