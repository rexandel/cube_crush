package com.rexandel.cube_crush.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_sessions",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["jti"], unique = true),
        Index(value = ["refresh_token_hash"], unique = true),
        Index(value = ["user_id"]),
        Index(value = ["access_token_expires_at"]),
        Index(value = ["refresh_token_expires_at"]),
        Index(value = ["is_revoked"])
    ]
)
data class UserSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "jti")
    val jti: String,

    @ColumnInfo(name = "refresh_token_hash")
    val refreshTokenHash: String,

    @ColumnInfo(name = "access_token_hash")
    val accessTokenHash: String?,

    @ColumnInfo(name = "access_token_expires_at")
    val accessTokenExpiresAt: Long,

    @ColumnInfo(name = "refresh_token_expires_at")
    val refreshTokenExpiresAt: Long,

    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "is_revoked", defaultValue = "0")
    val isRevoked: Boolean = false
)
