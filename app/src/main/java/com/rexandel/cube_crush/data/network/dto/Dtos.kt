package com.rexandel.cube_crush.data.network.dto

data class RegisterRequest(
    val nickname: String,
    val password: String
)

data class LoginRequest(
    val nickname: String,
    val password: String
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val userProfile: UserProfile
)

data class UserProfile(
    val id: Long,
    val nickname: String,
    val createdAt: String
)

data class ScoreRequest(
    val score: Int
)

data class Score(
    val id: Long,
    val userId: Long,
    val score: Int,
    val achievedAt: String
)

data class TopPlayer(
    val id: Long,
    val nickname: String,
    val score: Int,
    val achievedAt: String
)

data class UserStats(
    val id: Long,
    val nickname: String,
    val bestScoreAchievedAt: String?,
    val gamesPlayed: Long,
    val bestScore: Int,
    val averageScore: Double
)
