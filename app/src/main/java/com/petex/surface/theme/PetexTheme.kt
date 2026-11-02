package com.petex.surface.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/** All palette values live here; accents are chosen by state, not combined. */
object PetexColors {
    val Void = Color(0xFF06080C)
    val Glass = Color(0xFF0E131A).copy(alpha = 0.72f)
    val Cyan = Color(0xFF4FD1FF)
    val Violet = Color(0xFFA78BFA)
    val Amber = Color(0xFFFFB454)
    val Mint = Color(0xFF6EE7B7)
    val Ember = Color(0xFFFF5A5F)
    val TextPrimary = Color(0xFFE8F1F8)
    val TextDim = Color(0xFF7C8B9B)
    val White = Color.White
    val Hairline = White.copy(alpha = 0.08f)
}

private val petexTypography = Typography(
    bodyMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 15.sp,
        fontWeight = FontWeight.Normal, letterSpacing = (-0.1).sp, lineHeight = 21.75.sp),
    headlineMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 28.sp,
        fontWeight = FontWeight.Medium, letterSpacing = (-0.4).sp),
    titleMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 20.sp,
        fontWeight = FontWeight.Normal, letterSpacing = (-0.3).sp),
    labelMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 12.sp,
        fontWeight = FontWeight.Medium, letterSpacing = 0.6.sp)
)

@Composable
fun PetexTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = PetexColors.Cyan, background = PetexColors.Void,
            surface = PetexColors.Void, onSurface = PetexColors.TextPrimary,
            onBackground = PetexColors.TextPrimary
        ),
        typography = petexTypography,
        content = content
    )
}
