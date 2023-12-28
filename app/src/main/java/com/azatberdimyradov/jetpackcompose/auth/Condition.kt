package com.azatberdimyradov.jetpackcompose.auth

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azatberdimyradov.jetpackcompose.ui.theme.LightGreen
import com.azatberdimyradov.jetpackcompose.utils.Font.Geologica
import com.azatberdimyradov.jetpackcompose.utils.toPx

val conditionList = listOf(
    Condition(text = "At least 8 characters", false, "^.{8,}\$"),
    Condition(text = "Must contain an uppercase letter", false, "[A-Z]"),
    Condition(text = "Contains a symbol or number", false, "[\\p{Punct}\\d]")
)

@Composable
fun Conditions(
    modifier: Modifier = Modifier,
    conditions: List<Condition>
) {
    LazyColumn(modifier = modifier) {
        items(conditions) { item ->
            ConditionItem(condition = item, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Composable
fun ConditionItem(
    modifier: Modifier = Modifier,
    condition: Condition,
    primaryColor: Color = LightGreen,
    secondaryColor: Color = Color.DarkGray,
    iconSize: Dp = 22.dp
) {
    val checkBoxColor by animateColorAsState(
        targetValue = if (condition.isDone) primaryColor else secondaryColor,
        label = "Text color",
        animationSpec = tween(1000)
    )

    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Rounded.CheckCircle,
            contentDescription = "Done icon",
            tint = checkBoxColor,
            modifier = Modifier.size(iconSize)
        )
        Spacer(modifier = Modifier.width(8.dp))
        LinedText(
            text = condition.text,
            isLined = condition.isDone,
            primaryColor = primaryColor,
            secondaryColor = secondaryColor,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun LinedText(
    modifier: Modifier = Modifier,
    text: String,
    isLined: Boolean,
    primaryColor: Color = LightGreen,
    secondaryColor: Color = Color.Gray,
    textSize: TextUnit = 14.sp,
    textStyle: TextStyle = TextStyle.Default.copy(
        fontSize = textSize,
        fontWeight = FontWeight.Normal,
        fontFamily = Geologica
    )
) {
    var size by remember {
        mutableStateOf(Size.Zero)
    }

    var textWidth by remember {
        mutableStateOf(0f)
    }

    val textMeasurer = rememberTextMeasurer()

    val textLayoutResult = remember(text) {
        textMeasurer.measure(AnnotatedString(text), textStyle)
    }

    val color by animateColorAsState(
        targetValue = if (isLined) primaryColor else secondaryColor,
        label = "Text color",
        animationSpec = tween(1000)
    )

    val lineEnd by animateFloatAsState(
        targetValue = if (isLined) textWidth else 0f,
        label = "Line animation",
        animationSpec = tween(durationMillis = 1000)
    )

    val lineAlpha by animateFloatAsState(
        targetValue = if (isLined) 1f else 0f,
        label = "Line animation",
        animationSpec = tween(durationMillis = 1000)
    )

    Canvas(modifier = modifier) {
        size = this.size
        textWidth = textLayoutResult.size.width.toPx / 2.75f
        drawText(
            textLayoutResult = textLayoutResult,
            topLeft = Offset(
                x = 0f,
                y = (size.height / 2) - (textLayoutResult.size.height / 2)
            ),
            color = color
        )
        drawLine(
            color = Color.White,
            start = Offset(
                x = 0f,
                y = size.height / 2 + (textSize.toPx() / 6)
            ),
            end = Offset(
                x = lineEnd,
                y = size.height / 2 + (textSize.toPx() / 6)
            ),
            strokeWidth = (0.8).dp.toPx(),
            alpha = lineAlpha
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ConditionPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Conditions(
            conditions = conditionList,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
    }
}

data class Condition(
    val text: String,
    val isDone: Boolean,
    val regex: String
)