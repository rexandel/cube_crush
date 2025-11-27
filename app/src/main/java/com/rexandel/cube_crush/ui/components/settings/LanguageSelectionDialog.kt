package com.rexandel.cube_crush.ui.components.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rexandel.cube_crush.data.managers.AppLocale
import com.rexandel.cube_crush.data.managers.StringResources
import com.rexandel.cube_crush.ui.components.common.ButtonColor
import com.rexandel.cube_crush.ui.components.common.PixelButton

@Composable
fun LanguageSelectionDialog(
    onDismiss: () -> Unit,
    onLanguageSelected: (String) -> Unit,
    currentLocale: AppLocale
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = StringResources.selectLanguage,
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
                val isRussianSelected = currentLocale == AppLocale.RUSSIAN
                val isEnglishSelected = currentLocale == AppLocale.ENGLISH
                val isSystemSelected = currentLocale == AppLocale.SYSTEM

                PixelButton(
                    text = "Русский",
                    onClick = { onLanguageSelected("ru") },
                    buttonColor = if (isRussianSelected) ButtonColor.GREEN else ButtonColor.BLUE,
                    modifier = Modifier.fillMaxWidth()
                )

                PixelButton(
                    text = "English",
                    onClick = { onLanguageSelected("en") },
                    buttonColor = if (isEnglishSelected) ButtonColor.GREEN else ButtonColor.BLUE,
                    modifier = Modifier.fillMaxWidth()
                )

                PixelButton(
                    text = StringResources.systemLanguage,
                    onClick = { onLanguageSelected("system") },
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