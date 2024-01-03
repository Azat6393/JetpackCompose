package com.azatberdimyradov.jetpackcompose.tutorial_walkthrough

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp

data class TutorialWalkthroughState(
    var focusList: List<TutorialWalkthroughItem> = emptyList(),
    val focusItem: TutorialWalkthroughItem? = null
)

data class TutorialWalkthroughItem(
    var id: String,
    var focusOffset: Offset,
    var circleRadius: Dp,
    var description: String? = null,
    val isVisible: Boolean = false,
    val isShowed: Boolean = false
)