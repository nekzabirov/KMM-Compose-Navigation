package com.nekzabirov.navigatio.common

import androidx.compose.runtime.Composable

public actual fun getPlatformName(): String {
    return "Android"
}

@Composable
public fun UIShow() {
    App()
}