package com.rexandel.cube_crush.ui.components.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rexandel.cube_crush.R

@Composable
fun ErrorDialog(
    errorMessage: String,
    onDismiss: () -> Unit
) {
    if (errorMessage.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = stringResource(R.string.error_title)) },
            text = { Text(text = errorMessage) },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = stringResource(R.string.ok))
                }
            }
        )
    }
}
