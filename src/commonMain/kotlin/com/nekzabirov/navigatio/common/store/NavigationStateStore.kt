package com.nekzabirov.navigatio.common.store

import androidx.compose.runtime.Composable
import com.nekzabirov.navigatio.common.state.NavigationController

@Composable
public inline fun <reified T : Any> navigationState(
    navigationStateStore: NavigationController.NavigationStateStore? = LocalNavigationStateStore.current,
    key: Any = T::class,
    noinline destructor: ((T?) -> Unit)? = null,
    noinline constructor: () -> T,
): T {
    var value = navigationStateStore?.get(key)
    if (value !is T) {
        value = constructor().also {
            navigationStateStore?.set(
                key,
                NavigationController.Destructible(it, destructor as? ((Any?) -> Unit))
            )
        }
    }
    return value
}