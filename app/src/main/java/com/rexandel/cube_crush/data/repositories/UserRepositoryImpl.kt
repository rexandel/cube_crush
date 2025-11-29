package com.rexandel.cube_crush.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.rexandel.cube_crush.domain.repositories.UserRepository

class UserRepositoryImpl private constructor(context: Context) : UserRepository {
    private val sharedPref: SharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var INSTANCE: UserRepositoryImpl? = null

        fun getInstance(context: Context): UserRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserRepositoryImpl(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    override fun registerUser(email: String, password: String, nickname: String): Boolean {
        if (sharedPref.getString("email_$email", null) != null) {
            return false
        }

        if (sharedPref.getString("nickname_$nickname", null) != null) {
            return false
        }

        sharedPref.edit().apply {
            putString("email_$email", email)
            putString("password_$email", password)
            putString("nickname_$email", nickname)
            putString("nickname_$nickname", email)
            apply()
        }
        return true
    }

    override fun loginUser(email: String, password: String): Boolean {
        val savedPassword = sharedPref.getString("password_$email", "")
        return savedPassword == password
    }

    override fun setCurrentUser(email: String) {
        sharedPref.edit().putString("current_user", email).apply()
    }

    override fun getCurrentUser(): String? {
        return sharedPref.getString("current_user", null)
    }

    override fun getCurrentUserNickname(): String? {
        val currentUser = getCurrentUser()
        return if (currentUser != null) {
            sharedPref.getString("nickname_$currentUser", null)
        } else {
            null
        }
    }

    override fun logout() {
        sharedPref.edit().remove("current_user").apply()
    }

    override fun updateUserEmail(newEmail: String) {
        val currentUser = getCurrentUser()
        if (currentUser != null) {
            val currentPassword = sharedPref.getString("password_$currentUser", "")
            val currentNickname = sharedPref.getString("nickname_$currentUser", "")

            sharedPref.edit().apply {
                remove("email_$currentUser")
                remove("password_$currentUser")
                remove("nickname_$currentUser")
                remove("nickname_$currentNickname")

                putString("email_$newEmail", newEmail)
                putString("password_$newEmail", currentPassword)
                putString("nickname_$newEmail", currentNickname)
                putString("nickname_$currentNickname", newEmail)
                putString("current_user", newEmail)
                apply()
            }
        }
    }

    override fun updateUserNickname(newNickname: String) {
        val currentUser = getCurrentUser()
        if (currentUser != null) {
            val oldNickname = sharedPref.getString("nickname_$currentUser", "")

            sharedPref.edit().apply {
                if (oldNickname != null) {
                    remove("nickname_$oldNickname")
                }
                putString("nickname_$currentUser", newNickname)
                putString("nickname_$newNickname", currentUser)
                apply()
            }
        }
    }

    override fun verifyPassword(password: String): Boolean {
        val currentUser = getCurrentUser()
        if (currentUser != null) {
            val savedPassword = sharedPref.getString("password_$currentUser", "")
            return savedPassword == password
        }
        return false
    }

    override fun updatePassword(newPassword: String): Boolean {
        val currentUser = getCurrentUser()
        if (currentUser != null) {
            sharedPref.edit().putString("password_$currentUser", newPassword).apply()
            return true
        }
        return false
    }

    override fun isEmailExists(email: String): Boolean {
        return sharedPref.getString("email_$email", null) != null
    }

    override fun isNicknameExists(nickname: String): Boolean {
        return sharedPref.getString("nickname_$nickname", null) != null
    }
}