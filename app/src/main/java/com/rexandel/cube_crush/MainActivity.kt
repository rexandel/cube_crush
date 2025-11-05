package com.rexandel.cube_crush

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.rexandel.cube_crush.repository.UserRepository
import com.rexandel.cube_crush.ui.screens.GameScreen
import com.rexandel.cube_crush.ui.screens.SplashScreen
import com.rexandel.cube_crush.ui.screens.auth.LoginScreen
import com.rexandel.cube_crush.ui.screens.auth.RegisterScreen
import com.rexandel.cube_crush.ui.theme.CubeCrushTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CubeCrushTheme {
                var showSplash by remember { mutableStateOf(true) }
                var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Splash) }

                val context = LocalContext.current
                val userRepository = remember { UserRepository(context) }

                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(2000)
                    showSplash = false

                    val currentUser = userRepository.getCurrentUser()
                    currentScreen = if (currentUser != null) {
                        AppScreen.Game
                    } else {
                        AppScreen.Login
                    }
                }

                if (showSplash) {
                    SplashScreen()
                } else {
                    when (currentScreen) {
                        AppScreen.Splash -> SplashScreen()
                        AppScreen.Login -> LoginScreen(
                            onLoginSuccess = { currentScreen = AppScreen.Game },
                            onNavigateToRegister = { currentScreen = AppScreen.Register }
                        )
                        AppScreen.Register -> RegisterScreen(
                            onRegisterSuccess = { currentScreen = AppScreen.Game },
                            onBackToLogin = { currentScreen = AppScreen.Login }
                        )
                        AppScreen.Game -> GameScreen()
                    }
                }
            }
        }
    }
}

sealed class AppScreen {
    object Splash : AppScreen()
    object Login : AppScreen()
    object Register : AppScreen()
    object Game : AppScreen()
}