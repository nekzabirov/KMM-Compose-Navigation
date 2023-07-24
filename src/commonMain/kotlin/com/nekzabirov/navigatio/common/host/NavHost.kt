package com.nekzabirov.navigatio.common.host

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
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
    enterAnimation: EnterTransition = slideInHorizontally(initialOffsetX = { width -> -width }),
    exitAnimation: ExitTransition = slideOutHorizontally(targetOffsetX = { width -> -width }),
    navigationGraphBuilder: NavigationGraph.() -> Unit
): Unit = Box(modifier = modifier.background(MaterialTheme.colorScheme.background)) {
    val backStates = remember { navigationController.backStates }

    val currentNavBackState = backStates.last()

    AnimatedContent(
        targetState = currentNavBackState,
        transitionSpec = {
            val enter = this.targetState.enterAnimation ?: enterAnimation
            val exit = this.targetState.exitAnimation ?: exitAnimation

            ContentTransform(enter, exit)
        },
        contentKey = { it.destination }
    ) { it() }


    DisposableEffect(navigationController) {
        NavigationGraph().apply(navigationGraphBuilder).also {
            navigationController.destinations = it.destinations
        }
        navigationController.navigate(startRoute)
        onDispose {  }
    }
}