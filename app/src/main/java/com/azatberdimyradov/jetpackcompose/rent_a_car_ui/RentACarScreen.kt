package com.azatberdimyradov.jetpackcompose.rent_a_car_ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.azatberdimyradov.jetpackcompose.R
import com.azatberdimyradov.jetpackcompose.tutorial_walkthrough.TutorialWalkThrough
import com.azatberdimyradov.jetpackcompose.tutorial_walkthrough.TutorialWalkthroughItem
import com.azatberdimyradov.jetpackcompose.tutorial_walkthrough.TutorialWalkthroughStyle
import com.azatberdimyradov.jetpackcompose.tutorial_walkthrough.TutorialWalkthroughViewModel
import com.azatberdimyradov.jetpackcompose.tutorial_walkthrough.getFocusItem
import com.azatberdimyradov.jetpackcompose.ui.theme.DarkBackground
import com.azatberdimyradov.jetpackcompose.ui.theme.LightGreen
import com.azatberdimyradov.jetpackcompose.ui.theme.OnDarkBackground
import com.azatberdimyradov.jetpackcompose.ui.theme.SuccessGreen
import com.azatberdimyradov.jetpackcompose.utils.Font

@Composable
fun RentACarScreen() {
    val viewModel = hiltViewModel<TutorialWalkthroughViewModel>()
    val state = viewModel.focusState

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            RentACarHeader(addFocusItem = viewModel::addFocusItem)
            Spacer(modifier = Modifier.height(15.dp))
            Brands(
                modifier = Modifier.fillMaxWidth(),
                brands = CarBrand.getCars(),
                addFocusItem = viewModel::addFocusItem
            )
            Spacer(modifier = Modifier.height(30.dp))
            CarFilter(modifier = Modifier.fillMaxWidth(), addFocusItem = viewModel::addFocusItem)
        }
        if (state.focusItem != null && state.focusItem.isVisible) {
            TutorialWalkThrough(
                modifier = Modifier
                    .fillMaxSize(),
                id = state.focusItem.id,
                currentFocusOffset = state.focusItem.focusOffset,
                focusRadius = state.focusItem.circleRadius,
                style = TutorialWalkthroughStyle(
                    description = state.focusItem.description,
                    backgroundColor = Color.Black,
                    alpha = 0.85f
                ),
                onClick = {
                    viewModel.onShowed()
                }
            )
        }
    }
}

@Composable
fun CarFilter(modifier: Modifier = Modifier, addFocusItem: (TutorialWalkthroughItem) -> Unit) {
    CarFilterContainer(
        onSelect = { },
        modifier = modifier
    ) {
        Column(modifier = modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(50.dp))
            LocationItem(
                title = "Pick-Up",
                location = "Dubai Aiport",
                modifier = Modifier.fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
            ) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    thickness = 1.dp,
                    color = Color.LightGray.copy(alpha = 0.3f)
                )
                Icon(
                    painter = painterResource(id = R.drawable.change_icon),
                    contentDescription = "",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(35.dp)
                        .background(Color.White)
                        .drawBehind {
                            drawCircle(
                                color = Color.LightGray.copy(alpha = 0.3f),
                                radius = 17.5.dp.toPx(),
                                style = Stroke(width = 1.dp.toPx())
                            )
                        }
                        .padding(10.dp)
                        .align(Alignment.CenterEnd)
                )
            }
            LocationItem(
                title = "Drop Off",
                location = "Dubai Silicon Oasis",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(25.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
                thickness = 1.dp,
                color = Color.LightGray.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.height(25.dp))
            CalendarContainer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
            )
            Spacer(modifier = Modifier.height(70.dp))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = SuccessGreen,
                    contentColor = OnDarkBackground
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(horizontal = 25.dp)
                    .onGloballyPositioned {
                        addFocusItem(
                            it.getFocusItem("from_location", 170.dp,"Click to search car")
                        )
                    }
            ) {
                Text(
                    text = "Search Car",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Font.Geologica,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun CalendarContainer(modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.fillMaxWidth(0.4f)) {
            Text(
                text = "From",
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                fontFamily = Font.Geologica,
                color = Color.Gray
            )
            Text(
                text = "Jan 17 2023",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Font.Geologica,
                color = OnDarkBackground
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.baseline_calendar_month_24),
            contentDescription = "Calendar",
            tint = OnDarkBackground,
            modifier = Modifier.fillMaxWidth(0.3f)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp)
        ) {
            Text(
                text = "To",
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                fontFamily = Font.Geologica,
                color = Color.Gray
            )
            Text(
                text = "Jan 18 2023",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Font.Geologica,
                color = OnDarkBackground
            )
        }
    }
}

@Composable
fun LocationItem(modifier: Modifier = Modifier, title: String, location: String) {
    Row(
        modifier = modifier.padding(horizontal = 25.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location Item",
            tint = OnDarkBackground,
            modifier = Modifier.size(25.dp)
        )
        Spacer(modifier = Modifier.width(15.dp))
        Column {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Light,
                fontFamily = Font.Geologica,
                color = Color.Gray
            )
            Text(
                text = location,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Font.Geologica,
                color = OnDarkBackground
            )
        }
    }
}


@Composable
fun Brands(
    modifier: Modifier = Modifier, brands: List<CarBrand>,
    addFocusItem: (TutorialWalkthroughItem) -> Unit
) {
    Column(modifier) {
        Text(
            text = "Brans",
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow {
            item {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OnDarkBackground,
                        contentColor = Color.White
                    ), modifier = Modifier.onGloballyPositioned {
                        addFocusItem(
                            it.getFocusItem(
                                "select_all",
                                "Click select all Brans"
                            )
                        )
                    }
                ) {
                    Text(text = "All", fontSize = 15.sp, fontWeight = FontWeight.Normal)
                }
            }
            items(brands, key = { it.name }) { item ->
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OnDarkBackground,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.name,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = item.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun RentACarHeader(
    modifier: Modifier = Modifier,
    addFocusItem: (TutorialWalkthroughItem) -> Unit
) {
    Column(modifier = modifier) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            tint = Color.White,
            modifier = Modifier
                .size(50.dp)
                .drawBehind {
                    drawCircle(
                        color = OnDarkBackground,
                        radius = 25.dp.toPx(),
                        style = Stroke(width = 1.dp.toPx())
                    )
                }
                .padding(15.dp)
                .onGloballyPositioned {
                    addFocusItem(
                        it.getFocusItem("menu_item", 50.dp, "Click to open Menu")
                    )
                }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Text(
                text = "Rent a Car ",
                fontSize = 25.sp,
                fontFamily = Font.Geologica,
                fontWeight = FontWeight.Normal,
                color = Color.White,
            )
            Text(
                text = "Anytime",
                fontSize = 25.sp,
                fontFamily = Font.Geologica,
                fontWeight = FontWeight.Thin,
                color = Color.White,
            )
        }
        Text(
            text = "Anywhere",
            fontSize = 25.sp,
            fontFamily = Font.Geologica,
            fontWeight = FontWeight.Thin,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(15.dp))
    }
}

@Composable
fun CarFilterContainer(
    onSelect: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    var isFirstSection by remember {
        mutableStateOf(false)
    }

    val selectionFloat by
    animateFloatAsState(
        targetValue = if (isFirstSection) 0f else 0.5f, label = "line animation",
        animationSpec = tween(700)
    )

    val selectionLineStart by
    animateFloatAsState(
        targetValue = if (isFirstSection) 1f else 3f, label = "line animation",
        animationSpec = tween(if (!isFirstSection) 700 else 400)
    )

    val selectionLineEnd by
    animateFloatAsState(
        targetValue = if (isFirstSection) 1f else 3f, label = "line animation",
        animationSpec = tween(if (isFirstSection) 700 else 400)
    )

    val textColorForSame by animateColorAsState(
        targetValue = if (isFirstSection) DarkBackground else Color.LightGray,
        label = "textColorForSame"
    )
    val textColorForDifferent by animateColorAsState(
        targetValue = if (!isFirstSection) DarkBackground else Color.LightGray,
        label = "textColorForDifferent"
    )

    Column(modifier = modifier
        .drawBehind {
            val lineWidth = 50.dp.toPx()
            val x = size.width / 4

            drawRoundRect(
                color = Color.White,
                topLeft = Offset(x = 0f, y = 50.dp.toPx()),
                size = size,
                cornerRadius = CornerRadius(20.dp.toPx(), 20.dp.toPx())
            )

            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width / 2, 0f)
                lineTo(size.width / 2, 45.dp.toPx())
                lineTo(size.width / 2 + 100.dp.toPx(), 70.dp.toPx())
                lineTo(-100.dp.toPx(), 70.dp.toPx())
                lineTo(0f, 45.dp.toPx())
                close()
            }

            clipPath(
                path = Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(offset = Offset(0f, 0f), size = size),
                            cornerRadius = CornerRadius(20.dp.toPx(), 20.dp.toPx())
                        )
                    )
                }
            ) {
                translate(
                    left = size.width * selectionFloat,
                    top = 0f
                ) {
                    drawIntoCanvas { canvas ->
                        canvas.drawOutline(
                            outline = Outline.Generic(path),
                            paint = Paint().apply {
                                color = Color.White
                                pathEffect = PathEffect.cornerPathEffect(20.dp.toPx())
                            }
                        )
                    }
                }
            }

            drawLine(
                color = LightGreen,
                start = Offset(
                    x = (x * selectionLineStart) - lineWidth / 2,
                    y = 50.dp.toPx()
                ),
                end = Offset(
                    x = (x * selectionLineEnd) + lineWidth / 2,
                    y = 50.dp.toPx()
                ),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    ) {
        Row {
            Text(
                text = "Same Drop-Off",
                color = textColorForSame,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontFamily = Font.Geologica,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(0.5f)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        isFirstSection = true
                        onSelect(true)
                    }
            )
            Text(
                text = "Different Drop-Off",
                color = textColorForDifferent,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontFamily = Font.Geologica,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        isFirstSection = false
                        onSelect(false)
                    }
            )
        }
        Column(content = content)
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RentACarPreview() {
    RentACarScreen()
}