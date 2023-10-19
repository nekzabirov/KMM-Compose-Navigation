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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.nekzabirov.navigatio.common.backhandler.BackHandler
import com.nekzabirov.navigatio.common.state.LocalNavControllerStore
import com.nekzabirov.navigatio.common.state.NavigationController
import com.nekzabirov.navigatio.common.state.TopNavControllerStoreOwner
import com.nekzabirov.navigatio.common.store.LocalNavigationStateStore

@Composable
public fun NavHost(
    modifier: Modifier = Modifier.fillMaxSize(),
    navigationController: NavigationController,
    startRoute: String,
    enterAnimation: EnterTransition = fadeIn(),
    exitAnimation: ExitTransition = fadeOut(),
    navigationGraphBuilder: NavigationGraph.() -> Unit
) {
    val topNavControllerStore = LocalNavControllerStore.current
    val currentNavControllerStore = if (topNavControllerStore == null) {
        TopNavControllerStoreOwner.topNavControllerStore
    } else {
        topNavControllerStore.subNavControllers[navigationController.key] ?: throw Exception()
    }
    CompositionLocalProvider(
        LocalNavControllerStore provides currentNavControllerStore,
        LocalNavigationStateStore provides navigationController.stateStore
    ) {
        Box(modifier = modifier.background(MaterialTheme.colorScheme.background)) {
            val currentNavBackState by remember(navigationController) { navigationController.currentBackState }
                .collectAsState(null)
            BackHandler(
                enabled = currentNavBackState?.parent != null,
                onBack = { navigationController.popBack() }
            )

            AnimatedContent(
                targetState = currentNavBackState,
                transitionSpec = {
                    val enter = this.targetState?.enterAnimation ?: enterAnimation
                    val exit = this.targetState?.exitAnimation ?: exitAnimation

                    ContentTransform(enter, exit)
                },
                contentKey = { it?.destination }
            ) { it?.invoke() }

            LaunchedEffect(navigationController) {
                NavigationGraph().apply(navigationGraphBuilder).also {
                    navigationController.destinations = it.destinations
                }
                if (navigationController.currentBackState.value == null) {
                    navigationController.navigate(startRoute)
                }
            }
        }
    }
}
