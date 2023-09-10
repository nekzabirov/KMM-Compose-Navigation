package com.nekzabirov.navigatio.common.backhandler

import androidx.compose.runtime.Composable

public expect class OnBackPressedDispatcher() {
    @Composable
    public fun BackHandler(enabled: Boolean, onBack: () -> Unit)
}