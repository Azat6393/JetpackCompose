package com.azatberdimyradov.jetpackcompose.auth

import androidx.compose.runtime.Stable
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
class CustomTextFieldViewModel @Inject constructor() : ViewModel() {

    var state by mutableStateOf(AuthScreenState())
        private set

    fun onEmailChange(text: String) = viewModelScope.launch {
        state = state.copy(emailValue = text, isEmailError = isValidEmail(text))
    }

    fun onPasswordChange(text: String) = viewModelScope.launch {
        state = state.copy(passwordValue = text)
        checkConditions()
    }

    fun logIn() = viewModelScope.launch {
        if (!checkForValid()) return@launch
        state = state.copy(loading = true)
        delay(4000)
        state = state.copy(loading = false, isValid = true)
    }

    private fun checkConditions() = viewModelScope.launch {
        if (state.passwordValue.isBlank()) {
            state = state.copy(passwordConditions = conditionList, isPasswordError = true)
            return@launch
        }
        val newConditions = state.passwordConditions.map {
            val regex = Regex(it.regex)
            it.copy(isDone = regex.containsMatchIn(state.passwordValue))
        }
        state = state.copy(
            passwordConditions = newConditions,
            isPasswordError = newConditions.map { it.isDone }.contains(false)
        )
    }

    private fun isValidEmail(email: String): Boolean {
        val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$")
        return !regex.matches(email)
    }

    private fun checkForValid(): Boolean {
        return !state.isEmailError && !state.isPasswordError &&
                state.emailValue.isNotBlank() && state.passwordValue.isNotBlank()
    }
}

@Stable
data class AuthScreenState(
    val emailValue: String = "",
    val passwordValue: String = "",
    val isEmailError: Boolean = false,
    val isPasswordError: Boolean = false,
    val passwordConditions: List<Condition> = conditionList,
    val loading: Boolean = false,
    val isValid: Boolean = false
)