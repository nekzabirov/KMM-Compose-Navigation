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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.nekzabirov.navigatio.common.state.NavigationController
import com.nekzabirov.navigatio.common.store.LocalNavigationStateStore
import com.nekzabirov.navigatio.common.store.NavigationStateStore

@Composable
public fun NavHost(
    modifier: Modifier = Modifier.fillMaxSize(),
    navigationController: NavigationController,
    startRoute: String,
    enterAnimation: EnterTransition = fadeIn(),
    exitAnimation: ExitTransition = fadeOut(),
    navigationGraphBuilder: NavigationGraph.() -> Unit
) {
    CompositionLocalProvider(
        LocalNavigationStateStore provides NavigationStateStore(
            navigationController
        )
    ) {
        val navigationStateStore = LocalNavigationStateStore.current
        Box(modifier = modifier.background(MaterialTheme.colorScheme.background)) {
            val currentNavBackState by remember(navigationController) { navigationController.currentBackState }
                .collectAsState(null)
            AnimatedContent(
                targetState = currentNavBackState,
                transitionSpec = {
                    val enter = this.targetState?.enterAnimation ?: enterAnimation
                    val exit = this.targetState?.exitAnimation ?: exitAnimation

                    ContentTransform(enter, exit)
                },
                contentKey = { it?.destination }
            ) { it?.invoke() }

            DisposableEffect(navigationController) {
                NavigationGraph().apply(navigationGraphBuilder).also {
                    navigationController.destinations = it.destinations
                }
                navigationController.navigate(startRoute)
                onDispose {
                    navigationStateStore.clear()
                }
            }
        }
    }
}
