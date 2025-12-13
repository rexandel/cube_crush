package com.rexandel.cube_crush.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rexandel.cube_crush.data.database.entities.RevokedTokenEntity

@Dao
interface RevokedTokenDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(revokedToken: RevokedTokenEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM revoked_tokens WHERE jti = :jti)")
    suspend fun isRevoked(jti: String): Boolean
}
