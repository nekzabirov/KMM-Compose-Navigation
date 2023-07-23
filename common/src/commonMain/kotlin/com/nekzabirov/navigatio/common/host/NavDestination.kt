package com.nekzabirov.navigatio.common.host

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import com.nekzabirov.navigatio.common.argument.Bundle
import com.nekzabirov.navigatio.common.argument.NamedNavArgument

public class NavDestination(
    public val route: String,
    public val content: @Composable (Bundle) -> Unit
) {
    internal val routePattern = route.replace(Regex("\\{.*?\\}"), "(.*?)").toRegex()

    internal var enterAnimation: EnterTransition? = null
    internal var exitAnimation: ExitTransition? = null
    internal var args: List<NamedNavArgument> = emptyList()
}