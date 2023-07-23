package com.nekzabirov.navigatio.common.host

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.nekzabirov.navigatio.common.state.NavBackState
import com.nekzabirov.navigatio.common.state.NavigateRoute
import com.nekzabirov.navigatio.common.state.NavigationController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
public fun NavHost(
    modifier: Modifier = Modifier.fillMaxSize(),
    navigationController: NavigationController,
    startRoute: String,
    enterAnimation: EnterTransition = fadeIn(),
    exitAnimation: ExitTransition = fadeOut(),
    navigationGraphBuilder: NavigationGraph.() -> Unit
): Unit = Box(modifier = modifier.background(MaterialTheme.colorScheme.background)) {
    val navigationGraph = remember(startRoute) {
        NavigationGraph().apply(navigationGraphBuilder)
    }

    var currentNavBackState by remember {
        mutableStateOf(
            NavBackState(
                startRoute,
                destination = navigationGraph.findDestinations(startRoute)!!
            )
        )
    }

    AnimatedContent(
        targetState = currentNavBackState,
        transitionSpec = {
            val enter = this.targetState.enterAnimation ?: enterAnimation
            val exit = this.targetState.exitAnimation ?: exitAnimation

            ContentTransform(enter, exit)
        },
        contentKey = { it.destination }
    ) { it.invoke() }

    LaunchedEffect(navigationGraph) {
        navigationController.onNavigateRoute
            .onEach {
                currentNavBackState = if (it is NavigateRoute.Destination) {
                    val parent = if (it.option.launchSingleTop)
                        null
                    else if (it.option.finish)
                        currentNavBackState.parent
                    else if (it.option.popUpToRoute != null)
                        NavBackState(
                            route = it.option.popUpToRoute,
                            destination = navigationGraph.findDestinations(it.option.popUpToRoute)!!,
                        ).apply {
                            parent = currentNavBackState
                        }
                    else
                        currentNavBackState

                    NavBackState(
                        it.route,
                        destination = navigationGraph.findDestinations(it.route)!!,
                    ).apply {
                        this.parent = parent
                    }
                } else {
                    if (currentNavBackState.parent == null)
                        return@onEach

                    currentNavBackState.parent!!
                }

                navigationController._currentNavBackState.value = currentNavBackState
            }
            .launchIn(this)
    }
}