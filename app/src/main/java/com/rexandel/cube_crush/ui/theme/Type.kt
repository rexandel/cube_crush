package com.rexandel.cube_crush.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.rexandel.cube_crush.R

val CustomFontFamily = FontFamily(
    Font(R.font.press_start, FontWeight.Normal)
)

private val baseTextStyle = TextStyle(
    fontFamily = CustomFontFamily,
    fontWeight = FontWeight.Normal
)

val CustomTypography = Typography(
    displayLarge = baseTextStyle.copy(fontSize = 57.sp),
    displayMedium = baseTextStyle.copy(fontSize = 45.sp),
    displaySmall = baseTextStyle.copy(fontSize = 36.sp),
    headlineLarge = baseTextStyle.copy(fontSize = 32.sp),
    headlineMedium = baseTextStyle.copy(fontSize = 28.sp),
    headlineSmall = baseTextStyle.copy(fontSize = 24.sp),
    titleLarge = baseTextStyle.copy(fontSize = 22.sp),
    titleMedium = baseTextStyle.copy(fontSize = 16.sp),
    titleSmall = baseTextStyle.copy(fontSize = 14.sp),
    bodyLarge = baseTextStyle.copy(fontSize = 16.sp),
    bodyMedium = baseTextStyle.copy(fontSize = 14.sp),
    bodySmall = baseTextStyle.copy(fontSize = 12.sp),
    labelLarge = baseTextStyle.copy(fontSize = 14.sp),
    labelMedium = baseTextStyle.copy(fontSize = 12.sp),
    labelSmall = baseTextStyle.copy(fontSize = 11.sp)
)