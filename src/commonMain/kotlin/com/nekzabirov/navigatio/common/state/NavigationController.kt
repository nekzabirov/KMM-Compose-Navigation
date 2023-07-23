package com.nekzabirov.navigatio.common.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
public fun rememberNavController(): NavigationController = remember { NavigationController() }

public class NavigationController internal constructor(){
    private val _onNavigateRoute = Channel<NavigateRoute>(Channel.BUFFERED)
    internal val onNavigateRoute: Flow<NavigateRoute> = _onNavigateRoute.receiveAsFlow()

    internal val _currentNavBackState = MutableStateFlow<NavBackState?>(null)
    public val currentNavBackState: StateFlow<NavBackState?>
        get() = _currentNavBackState

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