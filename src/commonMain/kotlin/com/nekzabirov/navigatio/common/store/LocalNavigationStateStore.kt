package com.nekzabirov.navigatio.common.store

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf

public var LocalNavigationStateStore: ProvidableCompositionLocal<NavigationStateStore> =
    compositionLocalOf { error("No NavigationStateStore found") }