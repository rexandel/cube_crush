package com.rexandel.cube_crush.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rexandel.cube_crush.data.database.entities.UserSessionEntity

@Dao
interface UserSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: UserSessionEntity): Long

    @Update
    suspend fun update(session: UserSessionEntity)

    @Query("SELECT * FROM user_sessions WHERE jti = :jti")
    suspend fun findByJti(jti: String): UserSessionEntity?

    @Query("SELECT * FROM user_sessions WHERE user_id = :userId AND is_revoked = 0")
    suspend fun findActiveSessionsByUserId(userId: Long): List<UserSessionEntity>

    @Query("UPDATE user_sessions SET is_revoked = 1 WHERE jti = :jti")
    suspend fun revokeSession(jti: String)
}
