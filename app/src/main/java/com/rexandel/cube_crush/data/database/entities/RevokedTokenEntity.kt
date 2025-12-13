package com.rexandel.cube_crush.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "revoked_tokens",
    indices = [
        Index(value = ["expires_at"])
    ]
)
data class RevokedTokenEntity(
    @PrimaryKey
    @ColumnInfo(name = "jti")
    val jti: String,

    @ColumnInfo(name = "expires_at")
    val expiresAt: Long,

    @ColumnInfo(name = "revoked_at", defaultValue = "CURRENT_TIMESTAMP")
    val revokedAt: Long = System.currentTimeMillis()
)
