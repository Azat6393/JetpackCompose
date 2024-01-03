package com.azatberdimyradov.jetpackcompose.tutorial_walkthrough

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azatberdimyradov.jetpackcompose.ui.theme.DarkBlue
import com.azatberdimyradov.jetpackcompose.utils.Font

data class TutorialWalkthroughStyle(
    val description: String? = null,
    val textStyle: TutorialWalkthroughTextStyle = TutorialWalkthroughTextStyle(),
    val arrowStyle: TutorialWalkthroughArrow = TutorialWalkthroughArrow(),
    val alpha: Float = 0.5f,
    val backgroundColor: Color = DarkBlue
)

data class TutorialWalkthroughArrow(
    val wight: Dp = 15.dp,
    val height: Dp = 20.dp,
    val verticalSpace: Dp = 40.dp,
    val horizontalSpace: Dp = 30.dp,
    val alpha: Float = 0.5f,
    val stroke: Dp = 2.dp,
    val isDarkColor: Boolean = true
)

data class TutorialWalkthroughTextStyle(
    val textPadding: Dp = 10.dp,
    val cornerRadius: Dp = 10.dp,
    val space: Dp = 20.dp,
    val fontSize: TextUnit = 14.sp,
    val fontFamily: FontFamily = Font.Geologica,
    val color: Color = Color.Black,
    val backgroundColor: Color = Color.White,
    val padding: Dp = 10.dp
)