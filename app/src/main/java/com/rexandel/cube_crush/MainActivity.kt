package com.rexandel.cube_crush

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.rexandel.cube_crush.repository.UserRepository
import com.rexandel.cube_crush.ui.screens.GameScreen
import com.rexandel.cube_crush.ui.screens.MenuScreen
import com.rexandel.cube_crush.ui.screens.ProfileScreen
import com.rexandel.cube_crush.ui.screens.SplashScreen
import com.rexandel.cube_crush.ui.screens.auth.LoginScreen
import com.rexandel.cube_crush.ui.screens.auth.RegisterScreen
import com.rexandel.cube_crush.ui.theme.CubeCrushTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawableResource(android.R.color.transparent)

        setContent {
            CubeCrushTheme {
                androidx.compose.material3.Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    var showSplash by remember { mutableStateOf(true) }
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Splash) }

    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000)
        showSplash = false

        val currentUser = userRepository.getCurrentUser()
        currentScreen = if (currentUser != null) {
            AppScreen.Menu
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
                onLoginSuccess = { currentScreen = AppScreen.Menu },
                onNavigateToRegister = { currentScreen = AppScreen.Register }
            )
            AppScreen.Register -> RegisterScreen(
                onRegisterSuccess = { currentScreen = AppScreen.Menu },
                onBackToLogin = { currentScreen = AppScreen.Login }
            )
            AppScreen.Menu -> MenuScreen(
                onStartGame = { currentScreen = AppScreen.Game },
                onProfile = { currentScreen = AppScreen.Profile },
                onExit = {
                    (context as? android.app.Activity)?.finish()
                }
            )
            AppScreen.Game -> GameScreen(
                onExitToMenu = { currentScreen = AppScreen.Menu }
            )
            AppScreen.Profile -> ProfileScreen(
                onBackToMenu = { currentScreen = AppScreen.Menu }
            )
        }
    }
}

sealed class AppScreen {
    object Splash : AppScreen()
    object Login : AppScreen()
    object Register : AppScreen()
    object Menu : AppScreen()
    object Game : AppScreen()
    object Profile : AppScreen()
}