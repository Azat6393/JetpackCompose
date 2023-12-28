package com.azatberdimyradov.jetpackcompose.components

import android.graphics.PathMeasure
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedCheck(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    stroke: Dp = 10.dp
) {

    val pathPortion = remember {
        Animatable(initialValue = 0f)
    }

    LaunchedEffect(key1 = true) {
        pathPortion.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000
            )
        )
    }

    Box(
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val path = Path().apply {
                moveTo(0f, size.height / 2)
                lineTo((size.width / 6) * 2, size.height)
                lineTo(size.width, 0f)
            }
            val outPath = Path()
            PathMeasure().apply {
                setPath(path.asAndroidPath(), false)
                getSegment(0f, pathPortion.value * length, outPath.asAndroidPath(), true)
            }

            drawPath(
                path = outPath,
                color = color,
                style = Stroke(width = stroke.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}