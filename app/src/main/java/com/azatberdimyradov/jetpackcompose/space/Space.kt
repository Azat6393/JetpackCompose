package com.azatberdimyradov.canvarworkplace.space

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azatberdimyradov.canvarworkplace.space.StarModel.Companion.MOTION_SPEED_FAST
import com.azatberdimyradov.canvarworkplace.space.StarModel.Companion.MOTION_SPEED_SLOW
import com.azatberdimyradov.jetpackcompose.space.SpaceOnboardingContainer
import com.azatberdimyradov.jetpackcompose.space.SpaceOnboardingItem
import com.azatberdimyradov.jetpackcompose.utils.Font
import kotlinx.coroutines.launch

@Composable
fun Space(modifier: Modifier = Modifier) {

    val starModel by remember { mutableStateOf(StarModel()) }
    var time by remember { mutableStateOf(System.nanoTime()) }
    var isTurbo by remember { mutableStateOf(false) }
    var showOnboarding by remember { mutableStateOf(false) }

    val speed by animateFloatAsState(
        targetValue = if (isTurbo) MOTION_SPEED_FAST else MOTION_SPEED_SLOW,
        label = "Star speed animation",
        animationSpec = tween(2500),
        finishedListener = {
            if (!showOnboarding) {
                showOnboarding = true
                isTurbo = false
            }
        }
    )

    val starCount by animateIntAsState(
        targetValue = if (isTurbo) 7 else 2,
        label = "Star count animation",
        animationSpec = tween(2500)
    )

    val line by animateFloatAsState(
        targetValue = if (isTurbo) 1.15f else 1.01f,
        label = "Star line animation",
        animationSpec = tween(2500)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .drawBehind {
                val timeElapsed = System.nanoTime() - time
                time = System.nanoTime()
                starModel.update(timeElapsed, isTurbo, speed, starCount)

                val width = size.width
                val height = size.height

                starModel.stars.forEach { star ->
                    val start = Offset(
                        x = width / 2f + (width / 2f * star.x / star.z),
                        y = height / 2f + (height / 2f * star.y / star.z)
                    )
                    val end = Offset(
                        x = width / 2f + (width / 2f * star.x / (star.z / line)),
                        y = height / 2f + (height / 2f * star.y / (star.z / line))
                    )
                    if (line == 1.01f) {
                        drawPoints(
                            points = listOf(start),
                            pointMode = PointMode.Points,
                            color = Color.White,
                            strokeWidth = 3f
                        )
                    } else {
                        drawLine(
                            color = Color.White,
                            start = start,
                            end = end,
                            strokeWidth = 3f,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }
    ) {
        SpaceLaunchContainer(
            isVisible = { !isTurbo && !showOnboarding },
            getStarted = { isTurbo = !isTurbo },
            modifier = Modifier.fillMaxSize()
        )

        AnimatedVisibility(
            visible = showOnboarding,
            exit = fadeOut(animationSpec = tween(2000)),
            enter = fadeIn(animationSpec = tween(2000))
        ) {
            SpaceOnboardingContainer(modifier = Modifier.fillMaxSize())
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpaceOnboardingContainer(
    modifier: Modifier = Modifier
) {
    val onboardingItems = SpaceOnboardingItem.getItems()
    val pagerState = rememberPagerState(
        initialPage = 0, initialPageOffsetFraction = 0f
    )
    val scope = rememberCoroutineScope()

    Box(modifier = modifier) {
        HorizontalPager(
            pageCount = SpaceOnboardingItem.getItems().size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) { page ->
            SpaceOnboardingContainer(
                title = stringResource(id = onboardingItems[page].title),
                description = stringResource(id = onboardingItems[page].description),
                modifier = Modifier.fillMaxSize()
            )
        }
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(onboardingItems.size) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(10.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            IconButton(
                onClick = {
                    if (pagerState.currentPage != 2) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier
                    .size(50.dp)
                    .drawBehind {
                        drawCircle(
                            color = Color.White,
                            radius = 25.dp.toPx(),
                            style = Stroke(width = 1.dp.toPx())
                        )
                    }
                    .padding(15.dp)
            ) {
                AnimatedVisibility(
                    visible = pagerState.currentPage == 2,
                    exit = fadeOut(animationSpec = tween(500)),
                    enter = fadeIn(animationSpec = tween(500)),
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "done button",
                        tint = Color.White
                    )
                }
                AnimatedVisibility(
                    visible = pagerState.currentPage != 2,
                    exit = fadeOut(animationSpec = tween(500)),
                    enter = fadeIn(animationSpec = tween(500)),
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "next button",
                        tint = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun SpaceLaunchContainer(
    modifier: Modifier = Modifier,
    isVisible: () -> Boolean,
    getStarted: () -> Unit
) {
    Box(modifier = modifier) {
        AnimatedVisibility(
            visible = isVisible(),
            exit = fadeOut(animationSpec = tween(2000)),
            enter = fadeIn(animationSpec = tween(2000)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 40.dp)
        ) {
            Column {
                Text(
                    text = "Welcome to",
                    fontSize = 40.sp,
                    fontFamily = Font.Geologica,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
                Text(
                    text = "Space APP",
                    fontSize = 40.sp,
                    fontFamily = Font.Geologica,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }
        AnimatedVisibility(
            visible = isVisible(),
            exit = fadeOut(animationSpec = tween(1500)),
            enter = fadeIn(animationSpec = tween(1500)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        ) {
            Button(
                onClick = getStarted,
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    containerColor = Color.White
                )
            ) {
                Text(
                    text = "Get Started",
                    fontFamily = Font.Geologica,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SpacePreview() {
    Space()
}