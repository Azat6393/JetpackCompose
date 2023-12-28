package com.azatberdimyradov.jetpackcompose.rotaty_dail_lock

import android.graphics.Typeface
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import com.azatberdimyradov.jetpackcompose.R
import com.azatberdimyradov.jetpackcompose.ui.theme.ErrorRed
import com.azatberdimyradov.jetpackcompose.ui.theme.SuccessGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RotaryDialLockScreen(
    modifier: Modifier = Modifier
) {

    var password by remember {
        mutableStateOf("")
    }

    Column(modifier = modifier.padding(start = 15.dp, end = 15.dp)) {
        Spacer(modifier = Modifier.height(60.dp))

        NumberSelectorHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 15.dp)
        )

        Spacer(modifier = Modifier.height(60.dp))

        PasscodeController(
            modifier = Modifier
                .width(120.dp)
                .align(Alignment.End),
            passcode = { password },
            correctNumber = {
                password = ""
            },
            inCorrectNumber = {
                password = ""
            }
        )

        Spacer(modifier = Modifier.height(40.dp))

        RotaryDial(
            modifier = Modifier
                .size(450.dp)
                .padding(start = 10.dp, end = 10.dp),
            style = RotaryDialStyle(
                scaleWidth = 70.dp,
                radius = 140.dp,
                innerPadding = 8.dp,
                textSize = 28.sp
            ),
            onSelect = {
                password += it
            }
        )
    }
}

@Composable
fun PasscodeController(
    modifier: Modifier,
    passcode: () -> String,
    correctNumber: () -> Unit,
    inCorrectNumber: () -> Unit
) {
    val actualPassword = "2413"

    val circleColor = remember {
        mutableStateListOf(
            Animatable(Color.Black),
            Animatable(Color.Black),
            Animatable(Color.Black),
            Animatable(Color.Black)
        )
    }


    suspend fun changeCircleColor(color: Color) {
        for (i in 1 until 5) {
            circleColor[i - 1].animateTo(
                targetValue = color,
                animationSpec = tween(i * 40)
            )
        }
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = passcode()) {
        if (passcode().isNotBlank()) {
            val splitedPasscode = passcode().toCharArray()
            circleColor[splitedPasscode.size - 1].animateTo(
                targetValue = Color.White,
                animationSpec = tween(500)
            )

            if (splitedPasscode.size == 4) {
                if (passcode() == actualPassword) {
                    scope.launch {
                        changeCircleColor(SuccessGreen)
                        delay(1000L)
                        changeCircleColor(Color.Black)
                        correctNumber()
                    }
                } else {
                    scope.launch {
                        changeCircleColor(ErrorRed)
                        delay(1000L)
                        changeCircleColor(Color.Black)
                        inCorrectNumber()
                    }
                }
            }
        }
    }

    Canvas(modifier = modifier) {
        val circleRadius = 10.dp.toPx()
        for (i in 1 until 5) {
            drawCircle(
                radius = circleRadius,
                color = Color.Black,
                center = Offset(
                    x = i * (circleRadius * 2 + 5.dp.toPx()),
                    y = center.y
                )
            )
            drawCircle(
                radius = circleRadius - 3.dp.toPx(),
                color = circleColor[i - 1].value,
                center = Offset(
                    x = i * (circleRadius * 2 + 5.dp.toPx()),
                    y = center.y
                )
            )
        }
    }
}

@Composable
fun NumberSelectorHeader(modifier: Modifier) {
    val context = LocalContext.current
    val customTypeface = ResourcesCompat.getFont(context, R.font.alfa_slab_one)

    Text(
        text = "ENTER\nPASSCODE",
        fontSize = 40.sp,
        color = Color.Black,
        fontFamily = FontFamily(
            customTypeface ?: Typeface.create("Bold", Typeface.BOLD)
        ),
        lineHeight = 40.sp,
        letterSpacing = 2.sp,
        modifier = modifier
    )
}
