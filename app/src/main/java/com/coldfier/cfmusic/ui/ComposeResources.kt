package com.coldfier.cfmusic.ui

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.coldfier.cfmusic.R

val SfFontFamily = FontFamily(
    fonts = listOf(
        Font(
            resId = R.font.sf_font_bold,
            weight = FontWeight.Bold,
            style = FontStyle.Normal
        ),

        Font(
            resId = R.font.sf_font_regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        ),

        Font(
            resId = R.font.sf_font_medium,
            weight = FontWeight.Medium,
            style = FontStyle.Normal
        )
    )
)