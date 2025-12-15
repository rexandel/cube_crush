package com.rexandel.cube_crush.data.network

import com.rexandel.cube_crush.data.network.dto.AuthResponse
import com.rexandel.cube_crush.data.network.dto.LoginRequest
import com.rexandel.cube_crush.data.network.dto.RegisterRequest
import com.rexandel.cube_crush.data.network.dto.RefreshTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/logout")
    suspend fun logout(): Response<Unit>

    @POST("auth/refresh")
    fun refreshToken(@Body request: RefreshTokenRequest): retrofit2.Call<AuthResponse>
}
