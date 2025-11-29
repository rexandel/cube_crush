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

    override fun registerUser(email: String, password: String): Boolean {
        if (sharedPref.getString("email_$email", null) != null) {
            return false
        }

        sharedPref.edit().apply {
            putString("email_$email", email)
            putString("password_$email", password)
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

    override fun logout() {
        sharedPref.edit().remove("current_user").apply()
    }

    override fun updateUserEmail(newEmail: String) {
        val currentUser = getCurrentUser()
        if (currentUser != null) {
            val currentPassword = sharedPref.getString("password_$currentUser", "")

            sharedPref.edit().apply {
                remove("email_$currentUser")
                remove("password_$currentUser")

                putString("email_$newEmail", newEmail)
                putString("password_$newEmail", currentPassword)
                putString("current_user", newEmail)
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
}