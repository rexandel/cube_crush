package com.rexandel.cube_crush.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "scores",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["user_id"]),
        Index(value = ["score"]),
        Index(value = ["achieved_at"]),
        Index(value = ["user_id", "score"]),
        Index(value = ["user_id", "achieved_at"])
    ]
)
data class ScoreEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "score")
    val score: Int,

    @ColumnInfo(name = "achieved_at", defaultValue = "CURRENT_TIMESTAMP")
    val achievedAt: Long = System.currentTimeMillis()
)
