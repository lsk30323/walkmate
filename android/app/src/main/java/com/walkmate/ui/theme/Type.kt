package com.walkmate.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Compose Typography — type.xml 의 TextAppearance.WalkMate.* 와 평행 정의.
 *
 * 한국어 lineHeight: 영문 기본 1.4 대비 1.5 권장 (WCAG SC 1.4.12).
 * Pretendard 폰트는 res/font/pretendard.xml downloadable로 로드.
 */

private val Pretendard = FontFamily.Default // TODO: Pretendard ttf 번들 후 FontFamily(Font(R.font.pretendard, FontWeight.Normal)) 로 교체

val WalkMateTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight(700),
        fontSize = 57.sp,
        lineHeight = 80.sp,
        letterSpacing = (-0.02).sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight(700),
        fontSize = 28.sp,
        lineHeight = 39.sp, // 1.4
        letterSpacing = (-0.01).sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight(700),
        fontSize = 22.sp,
        lineHeight = 31.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight(600),
        fontSize = 18.sp,
        lineHeight = 25.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight(600),
        fontSize = 15.sp,
        lineHeight = 22.sp, // 1.5 한국어
    ),
    bodyLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight(400),
        fontSize = 16.sp,
        lineHeight = 24.sp, // 1.5
    ),
    bodyMedium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight(400),
        fontSize = 14.sp,
        lineHeight = 21.sp, // 1.5
    ),
    bodySmall = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight(400),
        fontSize = 12.sp,
        lineHeight = 18.sp, // 1.5
    ),
    labelLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight(600),
        fontSize = 14.sp,
        letterSpacing = 0.01.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight(500),
        fontSize = 12.sp,
    ),
)
