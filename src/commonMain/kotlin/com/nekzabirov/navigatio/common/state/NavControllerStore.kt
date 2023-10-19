package com.nekzabirov.navigatio.common.state

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import kotlin.native.concurrent.ThreadLocal

public data class NavControllerStore(val current: NavigationController) {
    internal val subNavControllers: MutableMap<Any, NavControllerStore> = mutableMapOf()
}

@ThreadLocal
public object TopNavControllerStoreOwner {
    public var topNavControllerStore: NavControllerStore? = null
}

public var LocalNavControllerStore: ProvidableCompositionLocal<NavControllerStore?> =
    compositionLocalOf { null }