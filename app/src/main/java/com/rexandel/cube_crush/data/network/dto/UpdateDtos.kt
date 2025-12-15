package com.rexandel.cube_crush.data.network.dto

data class UpdateNicknameRequest(
    val nickname: String
)

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)
