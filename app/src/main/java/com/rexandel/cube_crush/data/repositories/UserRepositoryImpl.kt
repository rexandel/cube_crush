package com.rexandel.cube_crush.data.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.rexandel.cube_crush.data.network.NetworkModule
import com.rexandel.cube_crush.data.network.dto.ChangePasswordRequest
import com.rexandel.cube_crush.data.network.dto.LoginRequest
import com.rexandel.cube_crush.data.network.dto.RegisterRequest
import com.rexandel.cube_crush.data.network.dto.UpdateNicknameRequest
import com.rexandel.cube_crush.data.network.dto.UserProfile
import com.rexandel.cube_crush.domain.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepositoryImpl private constructor(context: Context) : UserRepository {
    private val authApi = NetworkModule.getAuthApi(context)
    private val userApi = NetworkModule.getUserApi(context)
    private val sessionPref: SharedPreferences = context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
    private val TAG = "UserRepository"

    companion object {
        @Volatile
        private var INSTANCE: UserRepositoryImpl? = null

        fun getInstance(context: Context): UserRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserRepositoryImpl(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    private fun saveTokens(accessToken: String, refreshToken: String) {
        sessionPref.edit()
            .putString("access_token", accessToken)
            .putString("refresh_token", refreshToken)
            .apply()
    }

    private fun saveUserProfile(profile: UserProfile) {
        sessionPref.edit()
            .putString("current_user", profile.nickname)
            .putLong("user_id", profile.id)
            .apply()
    }

    override suspend fun registerUser(nickname: String, password: String): Boolean = withContext(Dispatchers.IO) {
        Log.d(TAG, "registerUser: nickname=$nickname")
        try {
            val response = authApi.register(RegisterRequest(nickname, password))
            Log.d(TAG, "registerUser: success")
            saveTokens(response.accessToken, response.refreshToken)
            saveUserProfile(response.userProfile)
            true
        } catch (e: Exception) {
            Log.e(TAG, "registerUser: error", e)
            e.printStackTrace()
            false
        }
    }

    override suspend fun loginUser(nickname: String, password: String): Boolean = withContext(Dispatchers.IO) {
        Log.d(TAG, "loginUser: nickname=$nickname")
        try {
            val response = authApi.login(LoginRequest(nickname, password))
            Log.d(TAG, "loginUser: success")
            saveTokens(response.accessToken, response.refreshToken)
            saveUserProfile(response.userProfile)
            true
        } catch (e: Exception) {
            Log.e(TAG, "loginUser: error", e)
            e.printStackTrace()
            false
        }
    }

    override suspend fun getCurrentUserNickname(): String? = withContext(Dispatchers.IO) {
        sessionPref.getString("current_user", null)
    }

    override suspend fun logout() = withContext(Dispatchers.IO) {
        Log.d(TAG, "logout")
        try {
            authApi.logout()
            Log.d(TAG, "logout: success")
        } catch (e: Exception) {
            Log.e(TAG, "logout: error", e)
        } finally {
            sessionPref.edit().clear().apply()
        }
        Unit
    }

    override suspend fun updateUserNickname(newNickname: String) = withContext(Dispatchers.IO) {
        Log.d(TAG, "updateUserNickname: newNickname=$newNickname")
        try {
            val profile = userApi.updateNickname(UpdateNicknameRequest(newNickname))
            Log.d(TAG, "updateUserNickname: success")
            saveUserProfile(profile)
        } catch (e: Exception) {
            Log.e(TAG, "updateUserNickname: error", e)
            e.printStackTrace()
        }
    }

    override suspend fun verifyPassword(password: String): Boolean = withContext(Dispatchers.IO) {
        Log.d(TAG, "verifyPassword")
        val nickname = getCurrentUserNickname() ?: return@withContext false
        try {
            authApi.login(LoginRequest(nickname, password))
            Log.d(TAG, "verifyPassword: success")
            true
        } catch (e: Exception) {
            Log.e(TAG, "verifyPassword: error", e)
            false
        }
    }

    override suspend fun updatePassword(currentPassword: String, newPassword: String): Boolean = withContext(Dispatchers.IO) {
        Log.d(TAG, "updatePassword")
        try {
            userApi.updatePassword(ChangePasswordRequest(currentPassword, newPassword))
            Log.d(TAG, "updatePassword: success")
            true
        } catch (e: Exception) {
            Log.e(TAG, "updatePassword: error", e)
            e.printStackTrace()
            false
        }
    }

    override suspend fun isNicknameExists(nickname: String): Boolean = withContext(Dispatchers.IO) {
        false
    }
}
