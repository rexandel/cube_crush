package com.rexandel.cube_crush.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rexandel.cube_crush.R
import com.rexandel.cube_crush.data.managers.StringResources
import com.rexandel.cube_crush.domain.game.ResourceLoader
import kotlinx.coroutines.delay

import com.rexandel.cube_crush.ui.components.common.ErrorDialog
import com.rexandel.cube_crush.data.managers.SoundManager

@Composable
fun SplashScreen(
    onLoadingComplete: () -> Unit = {}
) {
    val context = LocalContext.current
    val resourceLoader = remember { ResourceLoader(context) }
    val loadingState by resourceLoader.loadingState.collectAsState()
    val animatedProgress = remember { Animatable(0f) }
    val soundManager = remember { SoundManager.getInstance(context) }

    LaunchedEffect(Unit) {
        resourceLoader.loadAllResources()
    }

    LaunchedEffect(loadingState.progress) {
        animatedProgress.animateTo(
            targetValue = loadingState.progress,
            animationSpec = tween(durationMillis = 500)
        )
    }

    LaunchedEffect(loadingState.isComplete) {
        if (loadingState.isComplete && loadingState.error == null) {
            soundManager.playSound(SoundManager.Sound.LOADING_COMPLETE)
            delay(300)
            onLoadingComplete()
        }
    }

    if (loadingState.error != null) {
        ErrorDialog(
            errorMessage = loadingState.error!!,
            onDismiss = { 
                if (loadingState.isComplete) {
                    onLoadingComplete()
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.pixel_logo),
                contentDescription = StringResources.gameLogo,
                modifier = Modifier
                    .size(300.dp)
                    .padding(bottom = 40.dp)
            )

            LoadingProgressIndicator(
                progress = animatedProgress.value,
                currentStep = loadingState.currentStep,
                modifier = Modifier.fillMaxWidth(0.7f)
            )
        }
    }
}

@Composable
private fun LoadingProgressIndicator(
    progress: Float,
    currentStep: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (currentStep.isNotEmpty()) {
            Text(
                text = currentStep,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        }

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Round
        )

        Text(
            text = "${(progress * 100).toInt()}%",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}