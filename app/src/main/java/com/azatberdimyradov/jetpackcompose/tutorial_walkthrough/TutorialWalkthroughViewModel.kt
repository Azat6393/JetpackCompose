package com.azatberdimyradov.jetpackcompose.tutorial_walkthrough

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TutorialWalkthroughViewModel @Inject constructor() : ViewModel() {

    var focusState by mutableStateOf(TutorialWalkthroughState())
        private set

    init {
        viewModelScope.launch {
            delay(6000)
            showNextItem()
        }
    }

    fun addFocusItem(focusItem: TutorialWalkthroughItem) {
        val contains = focusState.focusList.find { it.id == focusItem.id } != null
        if (contains) return
        val newList = mutableListOf<TutorialWalkthroughItem>()
        newList.addAll(focusState.focusList)
        newList.add(focusItem)
        focusState = focusState.copy(focusList = newList, focusItem = focusState.focusList.firstOrNull())
    }

    fun onShowed() {
        focusState = focusState.copy(
            focusList = focusState.focusList.map {
                if (it.id == focusState.focusItem?.id) {
                    println(it)
                    it.copy(isShowed = true, isVisible = false)
                }
                else it
            }, focusItem = null
        )
        focusState.focusList.forEach { item ->
            if (!item.isShowed && !item.isVisible) {
                focusState = focusState.copy(focusItem = item)
                showNextItem()
                return@forEach
            }
        }
    }

    fun showNextItem() {
        focusState = focusState.copy(focusItem = focusState.focusItem?.copy(isVisible = true))
    }
}