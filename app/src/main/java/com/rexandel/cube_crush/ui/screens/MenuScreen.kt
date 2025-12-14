package com.rexandel.cube_crush.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rexandel.cube_crush.R
import com.rexandel.cube_crush.data.managers.StringResources
import com.rexandel.cube_crush.ui.components.common.ButtonColor
import com.rexandel.cube_crush.ui.components.common.PixelButton

@Composable
fun MenuScreen(
    onStartGame: () -> Unit,
    onSettings: () -> Unit,
    onScores: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.pixel_logo),
                contentDescription = StringResources.gameLogo,
                modifier = Modifier.size(350.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            PixelButton(
                text = StringResources.startGame,
                onClick = onStartGame,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                buttonColor = ButtonColor.YELLOW
            )

            Spacer(modifier = Modifier.height(16.dp))

            PixelButton(
                text = StringResources.scoresScreenTitle,
                onClick = onScores,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                buttonColor = ButtonColor.BLUE
            )

            Spacer(modifier = Modifier.height(16.dp))

            PixelButton(
                text = StringResources.settings,
                onClick = onSettings,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                buttonColor = ButtonColor.GREEN
            )
        }
    }
}