package com.azatberdimyradov.jetpackcompose.auth

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.azatberdimyradov.jetpackcompose.R
import com.azatberdimyradov.jetpackcompose.components.AnimatedCheck
import com.azatberdimyradov.jetpackcompose.utils.Font

@Composable
fun AuthScreen() {

    val viewModel = hiltViewModel<CustomTextFieldViewModel>()
    val state = viewModel.state

    val buttonWidth by animateDpAsState(
        targetValue = if (!state.loading) 300.dp else 120.dp,
        label = "",
        animationSpec = tween(500)
    )

    val buttonHeight by animateDpAsState(
        targetValue = if (!state.loading) 85.dp else 100.dp,
        label = "",
        animationSpec = tween(500)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(15.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.height(10.dp))
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Back button",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Signup",
                fontSize = 35.sp,
                fontFamily = Font.Geologica,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(60.dp))
            Column {
                TextFieldsContainer(
                    email = { state.emailValue },
                    password = { state.passwordValue },
                    onEmailChange = viewModel::onEmailChange,
                    onPasswordChange = viewModel::onPasswordChange,
                    isEmailError = { state.isEmailError },
                    isPasswordError = { state.isPasswordError },
                    passwordConditions = { state.passwordConditions })
                Spacer(modifier = Modifier.height(25.dp))
                AuthButtons()
            }
        }

        OutlinedButton(
            onClick = viewModel::logIn,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White
            ),
            border = BorderStroke(0.5.dp, Color.White),
            modifier = Modifier
                .width(buttonWidth)
                .height(buttonHeight)
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        ) {
            if (state.loading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text(text = "Continue")
            }
        }

        if (state.isValid) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                AnimatedCheck(
                    modifier = Modifier.size(100.dp),
                    stroke = 10.dp
                )
            }
        }
    }
}

@Composable
fun AuthButtons() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Icon(
            painter = painterResource(id = R.drawable.google),
            contentDescription = "google",
            modifier = Modifier.size(24.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(20.dp))
        Icon(
            painter = painterResource(id = R.drawable.facebook),
            contentDescription = "facebook",
            modifier = Modifier.size(24.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(20.dp))
        Icon(
            painter = painterResource(id = R.drawable.github),
            contentDescription = "github",
            modifier = Modifier.size(24.dp),
            tint = Color.White
        )
    }
}

@Composable
fun TextFieldsContainer(
    modifier: Modifier = Modifier,
    email: () -> String,
    password: () -> String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    isEmailError: () -> Boolean,
    isPasswordError: () -> Boolean,
    passwordConditions: () -> List<Condition>
) {
    Column(modifier = modifier.fillMaxWidth()) {
        CustomTextField(
            onTextChange = onEmailChange,
            text = email(),
            hint = "Email",
            conditions = null,
            isError = isEmailError(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        CustomTextField(
            onTextChange = onPasswordChange,
            text = password(),
            hint = "Password",
            conditions = passwordConditions(),
            isError = isPasswordError(),
            isPassword = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}