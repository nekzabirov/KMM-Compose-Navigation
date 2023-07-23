package com.nekzabirov.navigatio.common.state

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

public class NavigationController {
    private val _onNavigateRoute = Channel<NavigateRoute>(Channel.BUFFERED)
    internal val onNavigateRoute: Flow<NavigateRoute> = _onNavigateRoute.receiveAsFlow()

    public fun navigate(route: String, builder: NavigationOptionBuilder.() -> Unit = {}) {
        _onNavigateRoute.trySend(
            NavigateRoute.Destination(
                route,
                NavigationOptionBuilder().apply(builder).build()
            )
        )
    }

    public fun popBack() {
        _onNavigateRoute.trySend(NavigateRoute.Back)
    }
}