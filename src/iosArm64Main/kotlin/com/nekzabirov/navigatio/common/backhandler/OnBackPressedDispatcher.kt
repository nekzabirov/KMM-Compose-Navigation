package com.nekzabirov.navigatio.common.backhandler

import androidx.compose.runtime.Composable

public actual class OnBackPressedDispatcher actual constructor() {
    @Composable
    public actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {}
}