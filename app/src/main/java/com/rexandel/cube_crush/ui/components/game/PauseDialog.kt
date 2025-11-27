package com.rexandel.cube_crush.ui.components.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rexandel.cube_crush.R
import com.rexandel.cube_crush.data.managers.StringResources
import com.rexandel.cube_crush.ui.components.common.ButtonColor
import com.rexandel.cube_crush.ui.components.common.ButtonSize
import com.rexandel.cube_crush.ui.components.common.PixelButton

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onResume,
        title = {
            Text(
                StringResources.pause,
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    StringResources.gamePaused,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PixelButton(
                        text = StringResources.resume,
                        onClick = onResume,
                        buttonColor = ButtonColor.GREEN,
                        size = ButtonSize.SMALL,
                        iconResId = R.drawable.play_solid
                    )

                    PixelButton(
                        text = StringResources.restart,
                        onClick = onRestart,
                        buttonColor = ButtonColor.YELLOW,
                        size = ButtonSize.SMALL,
                        iconResId = R.drawable.arrow_solid
                    )

                    PixelButton(
                        text = StringResources.exitToMenu,
                        onClick = onExit,
                        buttonColor = ButtonColor.RED,
                        size = ButtonSize.SMALL,
                        iconResId = R.drawable.home_solid
                    )
                }
            }
        },
        dismissButton = {}
    )
}