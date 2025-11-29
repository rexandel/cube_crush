package com.rexandel.cube_crush.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rexandel.cube_crush.domain.repositories.UserRepository
import com.rexandel.cube_crush.domain.repositories.ScoreRepository

class GameViewModelFactory(
    private val userRepository: UserRepository,
    private val scoreRepository: ScoreRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(userRepository, scoreRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}