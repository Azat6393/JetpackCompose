package com.azatberdimyradov.jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.azatberdimyradov.jetpackcompose.rent_a_car_ui.RentACarScreen
import com.azatberdimyradov.jetpackcompose.tutorial_walkthrough.TutorialWalkThrough
import com.azatberdimyradov.jetpackcompose.tutorial_walkthrough.TutorialWalkthroughStyle
import com.azatberdimyradov.jetpackcompose.tutorial_walkthrough.TutorialWalkthroughViewModel
import com.azatberdimyradov.jetpackcompose.tutorial_walkthrough.getFocusItem
import com.azatberdimyradov.jetpackcompose.ui.theme.JetpackComposeTheme
import com.azatberdimyradov.jetpackcompose.ui.theme.PurpleGrey80
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RentACarScreen()
                }
            }
        }
    }
}