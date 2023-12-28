package com.azatberdimyradov.jetpackcompose.rotaty_dail_lock

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class RotaryDialStyle(
    val scaleWidth: Dp = 700.dp,
    val radius: Dp = 140.dp,
    val backgroundColor: Color = Color.Black,
    val sliderColor: Color = Color.White,
    val textColor: Color = Color.White,
    val textSize: TextUnit = 28.sp,
    val innerPadding: Dp = 5.dp
)
