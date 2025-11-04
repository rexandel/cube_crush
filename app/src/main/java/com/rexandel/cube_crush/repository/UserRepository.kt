package com.rexandel.cube_crush.repository

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
}