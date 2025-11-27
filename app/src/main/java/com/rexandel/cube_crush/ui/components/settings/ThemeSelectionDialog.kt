package com.rexandel.cube_crush.ui.components.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rexandel.cube_crush.data.managers.AppTheme
import com.rexandel.cube_crush.data.managers.StringResources
import com.rexandel.cube_crush.ui.components.common.ButtonColor
import com.rexandel.cube_crush.ui.components.common.PixelButton

@Composable
fun ThemeSelectionDialog(
    onDismiss: () -> Unit,
    onThemeSelected: (String) -> Unit,
    currentTheme: AppTheme
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = StringResources.selectTheme,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val isDarkSelected = currentTheme == AppTheme.DARK
                val isLightSelected = currentTheme == AppTheme.LIGHT
                val isSystemSelected = currentTheme == AppTheme.SYSTEM

                PixelButton(
                    text = StringResources.darkTheme,
                    onClick = { onThemeSelected("dark") },
                    buttonColor = if (isDarkSelected) ButtonColor.GREEN else ButtonColor.BLUE,
                    modifier = Modifier.fillMaxWidth()
                )

                PixelButton(
                    text = StringResources.lightTheme,
                    onClick = { onThemeSelected("light") },
                    buttonColor = if (isLightSelected) ButtonColor.GREEN else ButtonColor.BLUE,
                    modifier = Modifier.fillMaxWidth()
                )

                PixelButton(
                    text = StringResources.systemTheme,
                    onClick = { onThemeSelected("system") },
                    buttonColor = if (isSystemSelected) ButtonColor.GREEN else ButtonColor.BLUE,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            PixelButton(
                text = StringResources.cancel,
                onClick = onDismiss,
                buttonColor = ButtonColor.RED,
                modifier = Modifier.fillMaxWidth()
            )
        },
        dismissButton = {}
    )
}
