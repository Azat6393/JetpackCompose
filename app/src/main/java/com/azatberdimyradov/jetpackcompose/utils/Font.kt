package com.azatberdimyradov.jetpackcompose.utils

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.azatberdimyradov.jetpackcompose.R

object Font {
    val Geologica = FontFamily(
        fonts = listOf(
            Font(
                resId = R.font.geologica_regular,
                weight = FontWeight.Normal,
                style = FontStyle.Normal
            ),
            Font(
                resId = R.font.geologica_semi_bold,
                weight = FontWeight.SemiBold,
                style = FontStyle.Normal
            ),
            Font(
                resId = R.font.geologica_black,
                weight = FontWeight.Black,
                style = FontStyle.Normal
            ),
            Font(
                resId = R.font.geologica_bold,
                weight = FontWeight.Bold,
                style = FontStyle.Normal
            ),
            Font(
                resId = R.font.geologica_medium,
                weight = FontWeight.Medium,
                style = FontStyle.Normal
            ),
            Font(
                resId = R.font.geologica_light,
                weight = FontWeight.Light,
                style = FontStyle.Normal
            ),
            Font(
                resId = R.font.geologica_thin,
                weight = FontWeight.Thin,
                style = FontStyle.Normal
            ),
            Font(
                resId = R.font.geologica_extra_bold,
                weight = FontWeight.ExtraBold,
                style = FontStyle.Normal
            ),
        )
    )
}