package com.rexandel.cube_crush.data.network

import com.rexandel.cube_crush.data.network.dto.ChangePasswordRequest
import com.rexandel.cube_crush.data.network.dto.UpdateNicknameRequest
import com.rexandel.cube_crush.data.network.dto.UserProfile
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserApi {
    @GET("users/me")
    suspend fun getCurrentUser(): UserProfile

    @PATCH("users/me/nickname")
    suspend fun updateNickname(@Body request: UpdateNicknameRequest): UserProfile

    @PATCH("users/me/password")
    suspend fun updatePassword(@Body request: ChangePasswordRequest)
}
