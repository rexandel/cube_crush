package com.rexandel.cube_crush.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.rexandel.cube_crush.data.database.AppDatabase
import com.rexandel.cube_crush.data.database.entities.UserEntity
import com.rexandel.cube_crush.domain.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class UserRepositoryImpl private constructor(context: Context) : UserRepository {
    private val db = AppDatabase.getDatabase(context)
    private val userDao = db.userDao()
    private val sessionPref: SharedPreferences = context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var INSTANCE: UserRepositoryImpl? = null

        fun getInstance(context: Context): UserRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserRepositoryImpl(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    override suspend fun registerUser(email: String, password: String, nickname: String): Boolean = withContext(Dispatchers.IO) {
        if (userDao.findByEmail(email) != null) return@withContext false
        if (userDao.findByNickname(nickname) != null) return@withContext false

        val user = UserEntity(
            email = email,
            passwordHash = hashPassword(password),
            nickname = nickname
        )
        userDao.insert(user)
        true
    }

    override suspend fun loginUser(email: String, password: String): Boolean = withContext(Dispatchers.IO) {
        val user = userDao.findByEmail(email) ?: return@withContext false
        user.passwordHash == hashPassword(password)
    }

    override suspend fun setCurrentUser(email: String) = withContext(Dispatchers.IO) {
        sessionPref.edit().putString("current_user", email).apply()
    }

    override suspend fun getCurrentUser(): String? = withContext(Dispatchers.IO) {
        sessionPref.getString("current_user", null)
    }
    
    override suspend fun getCurrentUserNickname(): String? = withContext(Dispatchers.IO) {
        val email = getCurrentUser() ?: return@withContext null
        userDao.findByEmail(email)?.nickname
    }

    override suspend fun logout() = withContext(Dispatchers.IO) {
        sessionPref.edit().remove("current_user").apply()
    }

    override suspend fun updateUserEmail(newEmail: String) = withContext(Dispatchers.IO) {
        val currentEmail = getCurrentUser() ?: return@withContext
        val user = userDao.findByEmail(currentEmail) ?: return@withContext
        userDao.updateEmail(user.id, newEmail)
        setCurrentUser(newEmail)
    }

    override suspend fun updateUserNickname(newNickname: String) = withContext(Dispatchers.IO) {
        val currentEmail = getCurrentUser() ?: return@withContext
        val user = userDao.findByEmail(currentEmail) ?: return@withContext
        userDao.updateNickname(user.id, newNickname)
    }

    override suspend fun verifyPassword(password: String): Boolean = withContext(Dispatchers.IO) {
        val currentEmail = getCurrentUser() ?: return@withContext false
        val user = userDao.findByEmail(currentEmail) ?: return@withContext false
        user.passwordHash == hashPassword(password)
    }

    override suspend fun updatePassword(newPassword: String): Boolean = withContext(Dispatchers.IO) {
        val currentEmail = getCurrentUser() ?: return@withContext false
        val user = userDao.findByEmail(currentEmail) ?: return@withContext false
        userDao.updatePassword(user.id, hashPassword(newPassword))
        true
    }

    override suspend fun isEmailExists(email: String): Boolean = withContext(Dispatchers.IO) {
        userDao.findByEmail(email) != null
    }

    override suspend fun isNicknameExists(nickname: String): Boolean = withContext(Dispatchers.IO) {
        userDao.findByNickname(nickname) != null
    }
}