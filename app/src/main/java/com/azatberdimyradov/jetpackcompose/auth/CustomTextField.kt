package com.azatberdimyradov.jetpackcompose.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azatberdimyradov.jetpackcompose.R
import com.azatberdimyradov.jetpackcompose.components.AnimatedCheck
import com.azatberdimyradov.jetpackcompose.ui.theme.LightRed
import com.azatberdimyradov.jetpackcompose.utils.Font.Geologica

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    conditions: List<Condition>? = null,
    onTextChange: (String) -> Unit,
    text: String,
    hint: String,
    isError: Boolean = false,
    textStyle: TextStyle = TextStyle.Default.copy(
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = Geologica
    ),
    isPassword: Boolean = false
) {

    var textVisibility by remember {
        mutableStateOf(isPassword)
    }

    var onFocus by remember {
        mutableStateOf(false)
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = text,
                onValueChange = onTextChange,
                textStyle = textStyle,
                singleLine = true,
                visualTransformation = if (textVisibility) PasswordVisualTransformation() else VisualTransformation.None,
                cursorBrush = SolidColor(Color.White),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .onFocusChanged { onFocus = it.isFocused },
                decorationBox = @Composable { innerTextField ->
                    innerTextField.invoke()
                    if (text.isBlank()) {
                        Text(text = hint, color = Color.DarkGray, fontSize = textStyle.fontSize)
                    }
                }
            )
            if (text.isNotBlank() && !isError) {
                AnimatedCheck(
                    modifier = Modifier.size(15.dp),
                    stroke = 2.dp
                )
            }
            if (text.isNotBlank() && isPassword && isError) {
                Icon(
                    contentDescription = "Password",
                    painter = painterResource(id = R.drawable.baseline_remove_red_eye_24),
                    tint = Color.White,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            textVisibility = !textVisibility
                        }
                )
            }
        }
        Line(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp), color = when {
                isError -> LightRed
                text.isNotBlank() -> Color.White
                onFocus -> Color.White
                else -> Color.DarkGray
            },
            strokeWidth = if (onFocus) 2.dp else 1.dp
        )
        Spacer(modifier = Modifier.height(15.dp))
        if (conditions != null) {
            AnimatedVisibility(visible = onFocus) {
                Conditions(conditions = conditions, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun Line(
    modifier: Modifier,
    color: Color,
    strokeWidth: Dp = 1.dp
) {
    Canvas(modifier = modifier) {
        drawLine(
            color = color,
            start = Offset(
                x = 0f,
                y = size.height / 2
            ),
            end = Offset(
                x = size.width,
                y = size.height / 2
            ),
            strokeWidth = strokeWidth.toPx()
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CustomTextFieldPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        CustomTextField(
            conditions = conditionList,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            onTextChange = {},
            text = "iazat6393@gmail.com",
            hint = "email",
            isError = true
        )
    }
}