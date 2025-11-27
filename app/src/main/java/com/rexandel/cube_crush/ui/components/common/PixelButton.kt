package com.rexandel.cube_crush.ui.components.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rexandel.cube_crush.R
import com.rexandel.cube_crush.ui.theme.Black
import com.rexandel.cube_crush.ui.theme.CustomFontFamily

enum class ButtonColor {
    YELLOW, RED, BLUE, GREEN, PURPLE
}

enum class ButtonSize {
    SMALL, NORMAL
}

@Composable
fun PixelButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textColor: androidx.compose.ui.graphics.Color = Black,
    buttonColor: ButtonColor = ButtonColor.YELLOW,
    size: ButtonSize = ButtonSize.NORMAL,
    iconResId: Int? = null,
    width: Dp? = null,
    height: Dp? = null,
    iconSize: Dp? = null
) {
    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    val baseDimensions = when (size) {
        ButtonSize.SMALL -> Triple(64.dp, 64.dp, 12.sp)
        ButtonSize.NORMAL -> Triple(160.dp, 48.dp, 14.sp)
    }

    val finalWidth = width ?: baseDimensions.first
    val finalHeight = height ?: baseDimensions.second
    val fontSize = baseDimensions.third

    val defaultIconSize = when (size) {
        ButtonSize.SMALL -> 24.dp
        ButtonSize.NORMAL -> 48.dp
    }
    val finalIconSize = iconSize ?: defaultIconSize

    val buttonResId = when (size) {
        ButtonSize.SMALL -> when (buttonColor) {
            ButtonColor.YELLOW -> R.drawable.square_button_yellow
            ButtonColor.RED -> R.drawable.square_button_red
            ButtonColor.BLUE -> R.drawable.square_button_blue
            ButtonColor.GREEN -> R.drawable.square_button_green
            ButtonColor.PURPLE -> R.drawable.square_button_purple
        }
        ButtonSize.NORMAL -> when (buttonColor) {
            ButtonColor.YELLOW -> R.drawable.button_yellow
            ButtonColor.RED -> R.drawable.button_red
            ButtonColor.BLUE -> R.drawable.button_blue
            ButtonColor.GREEN -> R.drawable.button_green
            ButtonColor.PURPLE -> R.drawable.button_purple
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(width = finalWidth, height = finalHeight)
            .pointerInput(enabled) {
                if (enabled) {
                    detectTapGestures(
                        onPress = {
                            isPressed = true
                            val success = tryAwaitRelease()
                            isPressed = false
                            if (success) {
                                onClick()
                            }
                        }
                    )
                }
            }
    ) {
        Image(
            painter = rememberVectorPainter(
                ImageVector.vectorResource(id = buttonResId)
            ),
            contentDescription = "Pixel Button",
            modifier = Modifier
                .fillMaxSize()
                .alpha(if (isPressed && enabled) 0.85f else 1f)
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (iconResId != null) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = "Button Icon",
                    modifier = Modifier
                        .size(finalIconSize)
                        .alpha(if (isPressed && enabled) 0.85f else 1f)
                )
            } else {
                Text(
                    text = text,
                    color = if (enabled) {
                        if (isPressed) textColor.copy(alpha = 0.85f) else textColor
                    } else {
                        textColor.copy(alpha = 0.5f)
                    },
                    style = TextStyle(
                        fontFamily = CustomFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = fontSize,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.3.sp
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}