package com.rexandel.cube_crush.data.repositories

import android.content.Context
import android.content.SharedPreferences

class UserRepository(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun registerUser(email: String, password: String): Boolean {
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

    fun loginUser(email: String, password: String): Boolean {
        val savedPassword = sharedPref.getString("password_$email", "")
        return savedPassword == password
    }

    fun setCurrentUser(email: String) {
        sharedPref.edit().putString("current_user", email).apply()
    }

    fun getCurrentUser(): String? {
        return sharedPref.getString("current_user", null)
    }

    fun getHighScore(): Int {
        return sharedPref.getInt("high_score", 0)
    }

    fun saveHighScore(score: Int) {
        sharedPref.edit().putInt("high_score", score).apply()
    }

    fun updateHighScore(newScore: Int): Int {
        val currentHighScore = getHighScore()
        if (newScore > currentHighScore) {
            saveHighScore(newScore)
            return newScore
        }
        return currentHighScore
    }

    fun logout() {
        sharedPref.edit().remove("current_user").apply()
    }

    fun updateUserEmail(newEmail: String) {
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

    fun verifyPassword(password: String): Boolean {
        val currentUser = getCurrentUser()
        if (currentUser != null) {
            val savedPassword = sharedPref.getString("password_$currentUser", "")
            return savedPassword == password
        }
        return false
    }

    fun updatePassword(newPassword: String): Boolean {
        val currentUser = getCurrentUser()
        if (currentUser != null) {
            sharedPref.edit().putString("password_$currentUser", newPassword).apply()
            return true
        }
        return false
    }

    fun isEmailExists(email: String): Boolean {
        return sharedPref.getString("email_$email", null) != null
    }
}