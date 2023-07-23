package com.nekzabirov.navigatio.common.state

internal sealed interface NavigateRoute {
    data class Destination(val route: String, val option: NavigationOption) : NavigateRoute
    data object Back : NavigateRoute
}

internal data class NavigationOption(val popUpToRoute: String?, val launchSingleTop: Boolean, val finish: Boolean)

public class NavigationOptionBuilder {
    public var popUpToRoute: String? = null
    public var launchSingleTop: Boolean = false
    public var finish: Boolean = false

    internal fun build() =
        NavigationOption(popUpToRoute = popUpToRoute, launchSingleTop = launchSingleTop, finish = finish)
}