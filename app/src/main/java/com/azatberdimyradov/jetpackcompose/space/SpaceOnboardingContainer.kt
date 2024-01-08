package com.azatberdimyradov.jetpackcompose.space

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azatberdimyradov.jetpackcompose.R
import com.azatberdimyradov.jetpackcompose.utils.Font

data class SpaceOnboardingItem(
    val title: Int,
    val description: Int
) {
    companion object {
        fun getItems() = listOf(
            SpaceOnboardingItem(
                title = R.string.space_title_one,
                description = R.string.space_description_one
            ),
            SpaceOnboardingItem(
                title = R.string.space_title_two,
                description = R.string.space_description_two
            ),
            SpaceOnboardingItem(
                title = R.string.space_title_three,
                description = R.string.space_description_three
            )
        )
    }
}

@Composable
fun SpaceOnboardingContainer(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {

    Box(modifier = modifier) {
        Text(
            text = title,
            lineHeight = 85.sp,
            fontSize = 100.sp,
            fontFamily = Font.Geologica,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            modifier = Modifier
                .wrapContentHeight()
                .padding(start = 20.dp)
                .width(80.dp)
        )
        Text(
            text = description,
            fontSize = 17.sp,
            fontFamily = Font.Geologica,
            fontWeight = FontWeight.Thin,
            color = Color.White,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(end = 20.dp, top = 45.dp)
                .align(Alignment.TopEnd)
        )
    }
}