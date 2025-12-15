package com.rexandel.cube_crush.data.network

import android.content.Context
import android.util.Log
import com.rexandel.cube_crush.data.network.dto.RefreshTokenRequest
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    private val TAG = "AuthInterceptor"

    override fun intercept(chain: Interceptor.Chain): Response {
        val prefs = context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("access_token", null)
        
        val requestBuilder = chain.request().newBuilder()
        if (token != null) {
            Log.d(TAG, "Adding auth header")
            requestBuilder.addHeader("Authorization", "Bearer $token")
        } else {
            Log.d(TAG, "No auth token found")
        }
        
        val response = chain.proceed(requestBuilder.build())

        if (response.code == 401) {
            Log.d(TAG, "Received 401, attempting to refresh token")
            val refreshToken = prefs.getString("refresh_token", null)
            if (refreshToken != null) {
                synchronized(this) {
                    val currentToken = prefs.getString("access_token", null)
                    if (currentToken != null && currentToken != token) {
                        Log.d(TAG, "Token already refreshed, retrying request")
                        val newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $currentToken")
                            .build()
                        response.close()
                        return chain.proceed(newRequest)
                    }

                    try {
                        val authApi = NetworkModule.getAuthApi(context)
                        val refreshResponse = authApi.refreshToken(RefreshTokenRequest(refreshToken)).execute()

                        if (refreshResponse.isSuccessful) {
                            val newAuthResponse = refreshResponse.body()
                            if (newAuthResponse != null) {
                                Log.d(TAG, "Token refresh successful")
                                prefs.edit()
                                    .putString("access_token", newAuthResponse.accessToken)
                                    .putString("refresh_token", newAuthResponse.refreshToken)
                                    .apply()

                                val newRequest = chain.request().newBuilder()
                                    .header("Authorization", "Bearer ${newAuthResponse.accessToken}")
                                    .build()
                                response.close()
                                return chain.proceed(newRequest)
                            }
                        } else {
                            Log.e(TAG, "Token refresh failed: ${refreshResponse.code()}")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Token refresh error", e)
                    }
                }
            } else {
                Log.d(TAG, "No refresh token found")
            }
        }

        return response
    }
}
