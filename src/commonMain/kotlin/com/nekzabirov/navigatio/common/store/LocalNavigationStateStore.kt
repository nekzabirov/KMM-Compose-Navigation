package com.nekzabirov.navigatio.common.store

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.nekzabirov.navigatio.common.state.NavigationController

public var LocalNavigationStateStore: ProvidableCompositionLocal<NavigationController.NavigationStateStore> =
    compositionLocalOf { error("No NavigationStateStore found") }