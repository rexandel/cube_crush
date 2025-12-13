package com.rexandel.cube_crush.domain.repositories

interface UserRepository {
    suspend fun registerUser(email: String, password: String, nickname: String): Boolean
    suspend fun loginUser(email: String, password: String): Boolean
    suspend fun setCurrentUser(email: String)
    suspend fun getCurrentUser(): String?
    suspend fun getCurrentUserNickname(): String?
    suspend fun logout()
    suspend fun updateUserEmail(newEmail: String)
    suspend fun updateUserNickname(newNickname: String)
    suspend fun verifyPassword(password: String): Boolean
    suspend fun updatePassword(newPassword: String): Boolean
    suspend fun isEmailExists(email: String): Boolean
    suspend fun isNicknameExists(nickname: String): Boolean
}