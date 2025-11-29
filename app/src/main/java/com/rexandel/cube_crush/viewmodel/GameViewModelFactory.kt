package com.rexandel.cube_crush.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rexandel.cube_crush.data.repositories.UserRepository
import com.rexandel.cube_crush.data.repositories.ScoreRepository

class GameViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            val userRepository = UserRepository.getInstance(context)
            val scoreRepository = ScoreRepository.getInstance(context)
            return GameViewModel(userRepository, scoreRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}