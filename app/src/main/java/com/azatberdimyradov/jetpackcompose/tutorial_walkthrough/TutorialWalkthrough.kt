package com.azatberdimyradov.jetpackcompose.tutorial_walkthrough

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

@OptIn(ExperimentalTextApi::class)
@Composable
fun TutorialWalkThrough(
    modifier: Modifier = Modifier,
    id: String,
    currentFocusOffset: Offset,
    focusRadius: Dp = 0.dp,
    style: TutorialWalkthroughStyle,
    onClick: (id: String) -> Unit
) {
    var lastPoint by remember { mutableStateOf(currentFocusOffset) }
    var focusOffset by remember { mutableStateOf(currentFocusOffset) }
    val colors = getColors(style.arrowStyle.isDarkColor)
    val textMeasurer = rememberTextMeasurer()

    LaunchedEffect(key1 = currentFocusOffset){
        lastPoint = currentFocusOffset
        focusOffset = currentFocusOffset
    }

    Canvas(modifier = modifier
        .pointerInput(true) {
            detectDragGestures(
                onDragStart = { offset ->
                    lastPoint = offset
                },
                onDragEnd = {
                    val distance = sqrt(
                        (lastPoint.x - focusOffset.x).pow(2) +
                                (lastPoint.y - focusOffset.y).pow(2)
                    )
                    if (distance <= focusRadius.toPx()) {
                        onClick(id)
                    }
                    lastPoint = focusOffset
                }
            ) { _, dragAmount ->
                lastPoint += Offset(dragAmount.x, dragAmount.y)
            }
        }
        .pointerInput(true) {
            detectTapGestures { offset ->
                val distance = sqrt(
                    (offset.x - focusOffset.x).pow(2) +
                            (offset.y - focusOffset.y).pow(2)
                )
                if (distance <= focusRadius.toPx()) {
                    onClick(id)
                }
            }
        }
        .onSizeChanged {
            lastPoint = if (focusOffset == Offset.Unspecified) Offset(
                x = (it.width / 2).toFloat(),
                y = (it.height / 2).toFloat()
            ) else focusOffset
        }
    ) {
        val circlePath = android.graphics.Path().apply {
            addCircle(
                focusOffset.x, focusOffset.y, focusRadius.toPx(), android.graphics.Path.Direction.CW
            )
        }.asComposePath()
        clipPath(
            path = circlePath,
            clipOp = ClipOp.Difference
        ) {
            drawRect(
                color = style.backgroundColor,
                topLeft = Offset.Zero,
                size = size,
                alpha = style.alpha
            )

            val arrowWight = style.arrowStyle.wight.toPx()
            val arrowHeight = style.arrowStyle.height.toPx()

            val verticalSpace = style.arrowStyle.verticalSpace.toPx()
            val horizontalSpace = style.arrowStyle.horizontalSpace.toPx()

            val path = arrowPath(arrowWight, arrowHeight)

            val arrowRowCount = (size.width / (arrowWight + verticalSpace)).toInt()
            val arrowColumnCount = (size.height / (arrowHeight + horizontalSpace)).toInt()

            for (row in 0..arrowRowCount) {
                for (column in 0..arrowColumnCount) {
                    val x = (row * arrowWight) + (verticalSpace * row)
                    val y = (column * arrowHeight) + (horizontalSpace * column)

                    val degrees = atan2(lastPoint.x - x, y - lastPoint.y) * (180f / PI.toFloat())

                    val distance = sqrt(
                        (x - focusOffset.x).pow(2) +
                                (y - focusOffset.y).pow(2)
                    )
                    if (distance > (focusRadius.toPx() + 5.dp.toPx())) {
                        translate(left = x, top = y) {
                            rotate(degrees = degrees, pivot = Offset.Zero) {
                                drawPath(
                                    path = path,
                                    color = colors[row + column],
                                    alpha = style.arrowStyle.alpha,
                                    style = Stroke(style.arrowStyle.stroke.toPx())
                                )
                            }
                        }
                    }
                }
            }
            if (style.description != null) {
                val space = style.textStyle.space
                val textPadding = style.textStyle.textPadding
                val measuredText =
                    textMeasurer.measure(
                        AnnotatedString(style.description),
                        constraints = Constraints.fixedWidth((size.width / 2.5).toInt()),
                        style = TextStyle(
                            fontSize = style.textStyle.fontSize,
                            color = style.textStyle.color,
                            fontFamily = style.textStyle.fontFamily
                        )
                    )

                val rectWidth = measuredText.size.width + (textPadding * 2).toPx()
                val rectHeight = measuredText.size.height + (textPadding * 2).toPx()

                var rectX = focusOffset.x - (measuredText.size.width / 2) - textPadding.toPx()
                var rectY = focusOffset.y + (focusRadius + space).toPx()

                var isAlignBottom = false

                when {
                    rectX <= 0 -> {
                        rectX = ((0 - rectX) + rectX) * -1 + textPadding.toPx()
                    }

                    rectX + rectWidth >= size.width -> {
                        rectX -= ((rectX + rectWidth) - size.width) - textPadding.toPx()
                    }
                }

                if (rectY + rectHeight >= size.width) {
                    isAlignBottom = true
                    rectY = focusOffset.y - rectHeight - (focusRadius + space).toPx()
                }

                val rectPath = Path().apply {
                    if (isAlignBottom) {
                        moveTo(focusOffset.x, focusOffset.y - (focusRadius + (space / 2)).toPx())
                        lineTo(
                            focusOffset.x + space.toPx(),
                            focusOffset.y - (focusRadius + space).toPx()
                        )
                        lineTo(
                            focusOffset.x - space.toPx(),
                            focusOffset.y - (focusRadius + space).toPx()
                        )
                        moveTo(focusOffset.x, focusOffset.y - (focusRadius + (space / 2)).toPx())
                    } else {
                        moveTo(focusOffset.x, focusOffset.y + (focusRadius + (space / 2)).toPx())
                        lineTo(
                            focusOffset.x + space.toPx(),
                            focusOffset.y + (focusRadius + space).toPx()
                        )
                        lineTo(
                            focusOffset.x - space.toPx(),
                            focusOffset.y + (focusRadius + space).toPx()
                        )
                        moveTo(focusOffset.x, focusOffset.y + (focusRadius + (space / 2)).toPx())
                    }

                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(x = rectX - textPadding.toPx(), y = rectY),
                                size = Size(width = rectWidth, height = rectHeight)
                            ),
                            cornerRadius = CornerRadius(
                                style.textStyle.cornerRadius.toPx(),
                                style.textStyle.cornerRadius.toPx()
                            )
                        )
                    )
                }
                drawPath(rectPath, color = Color.White)
                drawText(
                    textLayoutResult = measuredText,
                    topLeft = Offset(x = rectX, y = rectY + textPadding.toPx())
                )
            }
        }
    }
}

private fun arrowPath(arrowWight: Float, arrowHeight: Float): Path {
    return Path().apply {
        moveTo(arrowWight * 0.40f, 0f)
        lineTo(arrowWight, arrowHeight * 0.75f)
        lineTo(arrowWight * 0.65f, arrowHeight * 0.75f)
        lineTo(arrowWight * 0.65f, arrowHeight)
        lineTo(arrowWight * 0.35f, arrowHeight)
        lineTo(arrowWight * 0.35f, arrowHeight * 0.75f)
        lineTo(0f, arrowHeight * 0.75f)
        close()
    }
}

fun LayoutCoordinates.getFocusItem(
    id: String,
    description: String? = null
): TutorialWalkthroughItem {
    val rect = this.boundsInRoot()
    val offset = rect.center
    return TutorialWalkthroughItem(
        id = id,
        focusOffset = offset,
        circleRadius = rect.width.dp / 4,
        description = description
    )
}

fun LayoutCoordinates.getFocusItem(
    id: String,
    circleRadius: Dp,
    description: String? = null
): TutorialWalkthroughItem {
    val rect = this.boundsInRoot()
    val offset = rect.center
    return TutorialWalkthroughItem(
        id = id,
        focusOffset = offset,
        circleRadius = circleRadius,
        description = description
    )
}

private fun getColors(isLightColor: Boolean): List<Color> {
    val colors = mutableListOf<Color>()
    if (isLightColor) {
        for (i in 1..100) {
            val red = (Math.random() * 128).toInt() + 128
            val green = (Math.random() * 128).toInt() + 128
            val blue = (Math.random() * 128).toInt() + 128

            val color = Color(red = red, green = green, blue = blue)
            colors.add(color)
        }
    } else {
        for (i in 1..100) {
            val red = (Math.random() * 128).toInt()
            val green = (Math.random() * 128).toInt()
            val blue = (Math.random() * 128).toInt()

            val color = Color(red = red, green = green, blue = blue)
            colors.add(color)
        }
    }
    return colors
}
