package com.nekzabirov.navigatio.common.backhandler

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner

@Composable
public actual fun BackHandler(
    enabled: Boolean,
    onBack: () -> Unit
) {
    val dispatcher = LocalLifecycleOwner.current
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current
    val callback = rememberUpdatedState(onBack)
    DisposableEffect(dispatcher, enabled) {
        val backCallback = object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                callback.value()
            }
        }
        backDispatcher?.onBackPressedDispatcher?.addCallback(backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}
