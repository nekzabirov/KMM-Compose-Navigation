package com.nekzabirov.navigatio.common.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.nekzabirov.navigatio.common.host.NavDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
public fun rememberNavController(): NavigationController = remember { NavigationController() }

public class NavigationController internal constructor() {
    internal var destinations: List<NavDestination> = emptyList()

    private val _backStates: SnapshotStateList<NavBackState> = mutableStateListOf()
    public val backStates: List<NavBackState>
        get() = _backStates

    private val _currentBackState = MutableStateFlow<NavBackState?>(null)
    public val currentBackState: StateFlow<NavBackState?>
        get() = _currentBackState

    public fun navigate(route: String, builder: NavigationOptionBuilder.() -> Unit = {}) {
        val navigationOption = NavigationOptionBuilder().apply(builder).build()

        val destination = findDestination(route) ?: return

        /*val parent = if (navigationOption.popUpToRoute != null) {
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
        else _backStates.lastOrNull()*/

        if (currentBackState.value != null)
            _backStates.add(currentBackState.value!!)

        if (navigationOption.launchSingleTop)
            _backStates.clear()
        else if (navigationOption.finish && _backStates.size > 1)
            _backStates.removeRange(1, _backStates.size)
        else if (navigationOption.popUpToRoute != null) {
            val parentIdx = _backStates
                .indexOfLast { it.destination.routePattern.matches(navigationOption.popUpToRoute) }

            if (parentIdx >= 0)
                _backStates.removeRange(parentIdx + 1, _backStates.size)
            else
                findDestination(navigationOption.popUpToRoute)?.let {
                    NavBackState(navigationOption.popUpToRoute, it)
                }?.also { _backStates.add(it) }
        }

        val navBackState = NavBackState(route, destination).apply {
            parent = _backStates.lastOrNull()
        }

        _currentBackState.value = (navBackState)
    }

    public fun popBack() {
        if (_backStates.isEmpty()) return

        _currentBackState.value = (backStates.last())

        _backStates.removeLast()
    }

    private fun findDestination(route: String) = destinations
        .findLast { it.routePattern.matches(route) }
}