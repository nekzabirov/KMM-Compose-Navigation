package com.nekzabirov.navigatio.common.host

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import com.nekzabirov.navigatio.common.argument.Bundle
import com.nekzabirov.navigatio.common.argument.NamedNavArgument

public class NavigationGraph {
    private val destinations = arrayListOf<NavDestination>()

    internal fun findDestinations(route: String) = destinations
        .findLast { it.routePattern.matches(route) }

    public fun composable(
        route: String,
        enterAnimation: EnterTransition? = null,
        exitAnimation: ExitTransition? = null,
        arguments: List<NamedNavArgument> = emptyList(),
        content: @Composable (Bundle) -> Unit
    ) {
        destinations.add(NavDestination(route, content).apply {
            this.enterAnimation = enterAnimation
            this.exitAnimation = exitAnimation
            this.args = arguments
        })
    }
}