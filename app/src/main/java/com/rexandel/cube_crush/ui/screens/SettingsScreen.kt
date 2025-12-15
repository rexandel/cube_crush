package com.rexandel.cube_crush.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.rexandel.cube_crush.R
import com.rexandel.cube_crush.domain.managers.AppTheme
import com.rexandel.cube_crush.data.managers.ThemeManager
import com.rexandel.cube_crush.data.managers.StringResources
import com.rexandel.cube_crush.data.managers.LocaleManager
import com.rexandel.cube_crush.domain.managers.AppLocale
import com.rexandel.cube_crush.data.repositories.UserRepositoryImpl
import com.rexandel.cube_crush.ui.components.settings.SettingsInfoItem
import com.rexandel.cube_crush.ui.components.settings.ThemeSelectionDialog
import com.rexandel.cube_crush.ui.components.settings.LanguageSelectionDialog
import com.rexandel.cube_crush.ui.components.settings.ChangePasswordDialog
import com.rexandel.cube_crush.ui.components.settings.LogoutConfirmationDialog
import com.rexandel.cube_crush.ui.components.settings.ChangeNicknameDialog
import com.rexandel.cube_crush.ui.components.common.PixelButton
import com.rexandel.cube_crush.ui.components.common.ButtonColor
import com.rexandel.cube_crush.ui.components.common.ButtonSize

import com.rexandel.cube_crush.ui.components.common.ErrorDialog

@Composable
fun SettingsScreen(
    onBackToMenu: () -> Unit,
    onLogout: () -> Unit,
    themeManager: ThemeManager,
    localeManager: LocaleManager,
    userRepository: UserRepositoryImpl
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var currentUser by remember { mutableStateOf<String?>(null) }
    var currentNickname by remember { mutableStateOf<String?>(null) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showChangeNicknameDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showUserMenu by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        try {
            currentNickname = userRepository.getCurrentUserNickname()
        } catch (e: Exception) {
            errorMessage = e.message ?: StringResources.getUnknownError(context)
            showError = true
        }
    }

    if (showError) {
        ErrorDialog(
            errorMessage = errorMessage,
            onDismiss = { showError = false }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(MaterialTheme.colorScheme.tertiary)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackToMenu,
                        modifier = Modifier.size(24.dp)
                    ) {
                        androidx.compose.foundation.Image(
                            painter = painterResource(id = R.drawable.arrow_left_solid),
                            contentDescription = StringResources.back,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = StringResources.settings,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Box {
                        IconButton(
                            onClick = { showUserMenu = true },
                            modifier = Modifier.size(24.dp)
                        ) {
                            androidx.compose.foundation.Image(
                                painter = painterResource(id = R.drawable.ellipses_vertical_solid),
                                contentDescription = StringResources.userMenu,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showUserMenu,
                            onDismissRequest = { showUserMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(StringResources.changeNickname) },
                                onClick = {
                                    showChangeNicknameDialog = true
                                    showUserMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(StringResources.changePassword) },
                                onClick = {
                                    showChangePasswordDialog = true
                                    showUserMenu = false
                                }
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 56.dp)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = StringResources.profile,
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = currentNickname ?: StringResources.notAvailable,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SettingsInfoItem(
                            label = StringResources.nickname,
                            value = currentNickname ?: StringResources.notAvailable,
                            showEditIcon = false
                        )

                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PixelButton(
                            text = "",
                            onClick = { showThemeDialog = true },
                            buttonColor = ButtonColor.PURPLE,
                            size = ButtonSize.SMALL,
                            iconResId = R.drawable.themes_solid,
                            modifier = Modifier.weight(1f)
                        )

                        PixelButton(
                            text = "",
                            onClick = { showLanguageDialog = true },
                            buttonColor = ButtonColor.GREEN,
                            size = ButtonSize.SMALL,
                            iconResId = R.drawable.translate_solid,
                            modifier = Modifier.weight(1f)
                        )

                        PixelButton(
                            text = "",
                            onClick = { showLogoutDialog = true },
                            buttonColor = ButtonColor.RED,
                            size = ButtonSize.SMALL,
                            iconResId = R.drawable.logout_solid,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            if (showChangePasswordDialog) {
                ChangePasswordDialog(
                    onDismiss = { showChangePasswordDialog = false },
                    onPasswordChanged = {
                        showChangePasswordDialog = false
                    },
                    userRepository = userRepository
                )
            }

            if (showChangeNicknameDialog) {
                ChangeNicknameDialog(
                    currentNickname = currentNickname ?: "",
                    onDismiss = { showChangeNicknameDialog = false },
                    onNicknameChanged = { newNickname ->
                        currentNickname = newNickname
                        showChangeNicknameDialog = false
                    },
                    userRepository = userRepository
                )
            }

            if (showLogoutDialog) {
                LogoutConfirmationDialog(
                    onDismiss = { showLogoutDialog = false },
                    onConfirm = {
                        scope.launch {
                            try {
                                userRepository.logout()
                                onLogout()
                            } catch (e: java.io.IOException) {
                                errorMessage = StringResources.getConnectionError(context)
                                showError = true
                            } catch (e: Exception) {
                                errorMessage = e.message ?: StringResources.getUnknownError(context)
                                showError = true
                            }
                        }
                    }
                )
            }

            if (showThemeDialog) {
                ThemeSelectionDialog(
                    onDismiss = { showThemeDialog = false },
                    onThemeSelected = { theme ->
                        val appTheme = when (theme) {
                            "dark" -> AppTheme.DARK
                            "light" -> AppTheme.LIGHT
                            else -> AppTheme.SYSTEM
                        }
                        themeManager.setTheme(appTheme)
                        showThemeDialog = false
                    },
                    currentTheme = themeManager.currentTheme
                )
            }

            if (showLanguageDialog) {
                LanguageSelectionDialog(
                    onDismiss = { showLanguageDialog = false },
                    onLanguageSelected = { language ->
                        val appLocale = when (language) {
                            "ru" -> AppLocale.RUSSIAN
                            "en" -> AppLocale.ENGLISH
                            else -> AppLocale.SYSTEM
                        }
                        localeManager.setLocale(appLocale)
                        showLanguageDialog = false

                        (context as? Activity)?.let { activity ->
                            activity.recreate()
                        }
                    },
                    currentLocale = localeManager.currentLocale
                )
            }
    }
}