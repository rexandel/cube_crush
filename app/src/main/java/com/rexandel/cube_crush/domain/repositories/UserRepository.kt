package com.rexandel.cube_crush.domain.repositories

interface UserRepository {
    suspend fun registerUser(nickname: String, password: String): Boolean
    suspend fun loginUser(nickname: String, password: String): Boolean
    suspend fun getCurrentUserNickname(): String?
    suspend fun logout()
    suspend fun updateUserNickname(newNickname: String)
    suspend fun verifyPassword(password: String): Boolean
    suspend fun updatePassword(currentPassword: String, newPassword: String): Boolean
    suspend fun isNicknameExists(nickname: String): Boolean
}