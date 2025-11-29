package com.rexandel.cube_crush.domain.repositories

interface UserRepository {
    fun registerUser(email: String, password: String, nickname: String): Boolean
    fun loginUser(email: String, password: String): Boolean
    fun setCurrentUser(email: String)
    fun getCurrentUser(): String?
    fun getCurrentUserNickname(): String?
    fun logout()
    fun updateUserEmail(newEmail: String)
    fun updateUserNickname(newNickname: String)
    fun verifyPassword(password: String): Boolean
    fun updatePassword(newPassword: String): Boolean
    fun isEmailExists(email: String): Boolean
    fun isNicknameExists(nickname: String): Boolean
}