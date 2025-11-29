package com.rexandel.cube_crush.ui.components.game

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
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
fun GameHeader(
    highScore: Int,
    score: Int,
    comboCount: Int = 0,
    onPauseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HighScoreSection(highScore = highScore)

            Spacer(modifier = Modifier.weight(1f))

            if (comboCount > 0) {
                CompactComboCounter(
                    comboCount = comboCount,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            PixelButton(
                text = "",
                onClick = onPauseClick,
                buttonColor = ButtonColor.YELLOW,
                size = ButtonSize.SMALL,
                iconResId = R.drawable.pause_solid,
                width = 48.dp,
                height = 48.dp,
                iconSize = 18.dp
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = StringResources.score(score),
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun HighScoreSection(highScore: Int) {
    Row {
        Image(
            painter = painterResource(id = R.drawable.crown_solid),
            contentDescription = StringResources.crownIcon,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = StringResources.highScore(highScore),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(top = 14.dp)
        )
    }
}

@Composable
fun CompactComboCounter(
    comboCount: Int,
    modifier: Modifier = Modifier
) {
    val scaleAnim = remember { Animatable(1f) }
    val rotationAnim = remember { Animatable(0f) }
    val alphaAnim = remember { Animatable(1f) }

    LaunchedEffect(comboCount) {
        if (comboCount > 0) {
            scaleAnim.animateTo(
                targetValue = 1.2f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            scaleAnim.animateTo(1f)

            rotationAnim.animateTo(3f, tween(durationMillis = 100))
            rotationAnim.animateTo(-3f, tween(durationMillis = 100))
            rotationAnim.animateTo(0f, tween(durationMillis = 100))

            alphaAnim.animateTo(0.7f, tween(durationMillis = 200))
            alphaAnim.animateTo(1f, tween(durationMillis = 200))
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "COMBO",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Text(
            text = "x${comboCount}",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            ),
            color = getComboColor(comboCount),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scaleAnim.value
                    scaleY = scaleAnim.value
                    rotationZ = rotationAnim.value
                    alpha = alphaAnim.value
                }
        )
    }
}

private fun getComboColor(comboCount: Int): Color {
    return when (comboCount) {
        1 -> Color(0xFF4CAF50)
        2 -> Color(0xFF2196F3)
        3 -> Color(0xFF9C27B0)
        4 -> Color(0xFFFF9800)
        5 -> Color(0xFFF44336)
        else -> Color(0xFFFFD700)
    }
}