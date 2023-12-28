package com.azatberdimyradov.jetpackcompose.rotaty_dail_lock

import android.graphics.Paint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.withRotation
import com.azatberdimyradov.jetpackcompose.R
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


@Composable
fun RotaryDial(
    modifier: Modifier = Modifier,
    style: RotaryDialStyle,
    onSelect: (number: Int) -> Unit
) {

    var size by remember {
        mutableStateOf(Size.Unspecified)
    }

    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    val numbersOffset = remember {
        mutableStateListOf<Offset>()
    }

    var selectedNumber by remember {
        mutableStateOf<Int?>(null)
    }

    var numberIsSelected by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val customTypeface = ResourcesCompat.getFont(context, R.font.alfa_slab_one)

    val angle by remember {
        mutableStateOf(Animatable(0f))
    }
    var dragStartedAngle by remember {
        mutableStateOf(0f)
    }

    var oldAngle by remember {
        mutableStateOf(0f)
    }

    Canvas(
        modifier = modifier
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = { offset ->
                        numbersOffset.forEachIndexed { index, _offset ->
                            val distance = sqrt(
                                (offset.x - _offset.x).pow(2) +
                                        (offset.y - _offset.y).pow(2)
                            )
                            if (distance <= (style.radius.toPx() / 5)) {
                                selectedNumber = if (index == 9) 0 else index + 1
                            }
                        }

                        dragStartedAngle = -atan2(
                            circleCenter.x - offset.x,
                            circleCenter.y - offset.y
                        ) * (180f / PI.toFloat())
                    },
                    onDragEnd = {
                        oldAngle = angle.value
                        scope.launch {
                            angle.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(
                                    durationMillis = 1000
                                )
                            )
                        }
                        oldAngle = 0f
                        dragStartedAngle = 0f
                        selectedNumber = null
                        numberIsSelected = false
                    }
                ) { change, dragAmount ->
                    val touchAngle = -atan2(
                        circleCenter.x - change.position.x,
                        circleCenter.y - change.position.y
                    ) * (180f / PI.toFloat())

                    var newAngle = oldAngle + (touchAngle - dragStartedAngle)

                    val maxValue = if (selectedNumber == null) 0f else getMaxAngleByNumber(
                        selectedNumber!!
                    )

                    if (maxValue != 0f && !numberIsSelected && angle.value == maxValue) {
                        onSelect(selectedNumber!!)
                        numberIsSelected = true
                    }
                    if (newAngle < 0) {
                        newAngle *= -1
                        newAngle = 360 - newAngle
                    }

                    scope.launch {
                        angle.animateTo(
                            targetValue = newAngle.coerceIn(
                                minimumValue = 0f,
                                maximumValue = maxValue
                            ),
                            animationSpec = tween(0)
                        )
                    }
                }
            }
    ) {
        size = this.size
        circleCenter = this.center
        val radiusInPx = style.radius.toPx()

        val outerRadius = radiusInPx + style.scaleWidth.toPx() / 2f + style.innerPadding.toPx()
        val innerRadius = radiusInPx - style.scaleWidth.toPx() / 2f - style.innerPadding.toPx()

        val numberCircleRadius = radiusInPx / 5

        drawCircle(
            color = style.backgroundColor,
            radius = outerRadius,
            center = center
        )
        drawCircle(
            color = style.sliderColor,
            radius = innerRadius,
            center = center
        )

        val clipSliderPath = Path().apply {
            var index = 1
            numbersOffset.clear()
            for (i in 11.downTo(1)) {
                index++
                val angleInRad = (index * 30 - 30f) * (PI / 180f).toFloat()
                val offset = Offset(
                    x = radiusInPx * cos(angleInRad) + center.x,
                    y = radiusInPx * sin(angleInRad) + center.y
                )
                numbersOffset.add(offset)
                addArc(
                    oval = Rect(
                        radius = numberCircleRadius,
                        center = offset
                    ),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 360f
                )
            }
            numbersOffset.reverse()
        }

        var index = 1
        for (i in 11.downTo(1)) {
            index++
            val angleInRad = (index * 30 - 30f) * (PI / 180f).toFloat()
            if (i != 11) {
                drawContext.canvas.nativeCanvas.apply {
                    val x = radiusInPx * cos(angleInRad) + center.x
                    val y =
                        radiusInPx * sin(angleInRad) + center.y + (style.textSize.toPx() / 2.5f)
                    drawText(
                        if (i == 10) abs(0).toString()
                        else abs(i).toString(),
                        x, y,
                        Paint().apply {
                            textSize = style.textSize.toPx()
                            textAlign = Paint.Align.CENTER
                            color = Color.White.toArgb()
                            typeface = customTypeface
                        }
                    )
                }
            } else {
                drawCircle(
                    color = style.sliderColor,
                    radius = numberCircleRadius * 0.5f,
                    center = Offset(
                        x = radiusInPx * cos(angleInRad) + center.x,
                        y = radiusInPx * sin(angleInRad) + center.y
                    )
                )
            }
        }

        drawContext.canvas.nativeCanvas.apply {
            withRotation(
                degrees = angle.value,
                pivotX = center.x,
                pivotY = center.y
            ) {
                clipPath(clipSliderPath, clipOp = ClipOp.Difference) {
                    drawArc(
                        color = style.sliderColor,
                        topLeft = Offset(
                            x = center.x - radiusInPx,
                            y = center.y - radiusInPx
                        ),
                        size = Size(
                            width = radiusInPx * 2,
                            height = radiusInPx * 2
                        ),
                        startAngle = 60f,
                        sweepAngle = 270f,
                        useCenter = false,
                        style = Stroke(
                            width = style.scaleWidth.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }
            }
        }
    }
}

private fun getMaxAngleByNumber(number: Int): Float {
    return when (number) {
        1 -> 60f
        2 -> 90f
        3 -> 120f
        4 -> 150f
        5 -> 180f
        6 -> 210f
        7 -> 240f
        8 -> 270f
        9 -> 300f
        else -> 330f
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun NumberSelectorPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        RotaryDial(
            modifier = Modifier
                .size(450.dp)
                .align(Alignment.Center),
            style = RotaryDialStyle(
                scaleWidth = 70.dp,
                radius = 140.dp,
                innerPadding = 8.dp,
                textSize = 28.sp
            ),
            onSelect = {

            }
        )
    }
}