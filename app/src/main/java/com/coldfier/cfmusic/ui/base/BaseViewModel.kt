package com.coldfier.cfmusic.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel: ViewModel() {

    private fun handleError(e: Exception) {

    }

    fun launchInIOCoroutine(func: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            func.invoke()
        }
    }
}