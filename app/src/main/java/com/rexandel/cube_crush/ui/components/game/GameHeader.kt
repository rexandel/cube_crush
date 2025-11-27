package com.rexandel.cube_crush.ui.components.game

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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