package com.rexandel.cube_crush.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rexandel.cube_crush.data.database.daos.ScoreDao
import com.rexandel.cube_crush.data.database.daos.RevokedTokenDao
import com.rexandel.cube_crush.data.database.daos.UserDao
import com.rexandel.cube_crush.data.database.daos.UserSessionDao
import com.rexandel.cube_crush.data.database.entities.ScoreEntity
import com.rexandel.cube_crush.data.database.entities.RevokedTokenEntity
import com.rexandel.cube_crush.data.database.entities.UserEntity
import com.rexandel.cube_crush.data.database.entities.UserSessionEntity
import com.rexandel.cube_crush.data.database.views.TopPlayerView
import com.rexandel.cube_crush.data.database.views.UserStatsView

@Database(
    entities = [
        UserEntity::class,
        ScoreEntity::class,
        UserSessionEntity::class,
        RevokedTokenEntity::class
    ],
    views = [
        TopPlayerView::class,
        UserStatsView::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun scoreDao(): ScoreDao
    abstract fun userSessionDao(): UserSessionDao
    abstract fun revokedTokenDao(): RevokedTokenDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cube_crush_game"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
