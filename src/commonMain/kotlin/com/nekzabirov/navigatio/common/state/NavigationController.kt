package com.nekzabirov.navigatio.common.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.nekzabirov.navigatio.common.host.NavDestination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
public fun rememberNavController(): NavigationController = remember { NavigationController() }

public class NavigationController internal constructor() {
    internal var destinations: List<NavDestination> = emptyList()

    private val _backStates: SnapshotStateList<NavBackState> = mutableStateListOf()
    public val backStates: List<NavBackState>
        get() = _backStates

    public fun navigate(route: String, builder: NavigationOptionBuilder.() -> Unit = {}) {
        val navigationOption = NavigationOptionBuilder().apply(builder).build()

        val destination = findDestination(route) ?: return

        val parent = if (navigationOption.popUpToRoute != null) {
            val backState = _backStates
                .find { it.destination.routePattern.matches(navigationOption.popUpToRoute) }

            if (backState != null)
                backState
            else {
                val pDestination = findDestination(navigationOption.popUpToRoute)

                if (pDestination == null)
                    null
                else
                    NavBackState(navigationOption.popUpToRoute, destination)
                        .apply { this.parent = _backStates.lastOrNull() }
            }
        }
        else if (navigationOption.finish) _backStates.lastOrNull()
        else _backStates.lastOrNull()

        if (navigationOption.launchSingleTop)
            _backStates.clear()

        val navBackState = NavBackState(route, destination)
            .apply { this.parent = parent }

        if (parent != null) {
            val parentIdx = _backStates.indexOf(parent)

            if (parentIdx < 0)
                _backStates.add(parent)
            else
                _backStates.removeRange(parentIdx, _backStates.size)
        }

        _backStates.add(navBackState)
    }

    public fun popBack() {
        if (_backStates.isEmpty()) return

        _backStates.removeLast()
    }

    private fun findDestination(route: String) = destinations
        .findLast { it.routePattern.matches(route) }
}