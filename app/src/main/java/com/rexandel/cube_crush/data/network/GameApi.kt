package com.rexandel.cube_crush.data.network

import com.rexandel.cube_crush.data.network.dto.Score
import com.rexandel.cube_crush.data.network.dto.ScoreRequest
import com.rexandel.cube_crush.data.network.dto.TopPlayer
import com.rexandel.cube_crush.data.network.dto.UserStats
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface GameApi {
    @GET("game/history")
    suspend fun getHistory(): List<Score>

    @POST("game/score")
    suspend fun submitScore(@Body request: ScoreRequest): Score

    @GET("game/top")
    suspend fun getTopPlayers(): List<TopPlayer>

    @GET("game/stats")
    suspend fun getUserStats(): UserStats
}
