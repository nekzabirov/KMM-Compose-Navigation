package com.nekzabirov.navigatio.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.nekzabirov.navigatio.common.argument.NavType
import com.nekzabirov.navigatio.common.argument.navArgument

private val navigationController = NavigationController()

@Composable
internal fun App() {
    NavHost(navigationController = navigationController, startRoute = "a") {
        composable("a") { ScreenA() }

        composable(
            "b/{name}", arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { ScreenB(it.getString("name")!!) }

        composable("c?id={id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) { ScreenC(it.getInt("id")!!) }
    }
}

@Composable
private fun ScreenA() {
    var count by rememberSaveable(1) { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize().background(Color.Blue).clickable {
        navigationController.navigate("b/nek")
        count++
    }) {
        Text(count.toString(), fontSize = 58.sp)
    }
}

@Composable
private fun ScreenB(name: String) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Red).clickable {
        navigationController.navigate("c?id=66") {
            finish = true
        }
    }) {
        Button({
            navigationController.popBack()
        }) {
            Text(name)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            println("DisposableEffect C")
        }
    }

    LaunchedEffect(Unit) {
        println("LaunchedEffect C")
    }
}

@Composable
private fun ScreenC(id: Int) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Green).clickable {
        navigationController.popBack()
    }) {
        Text(id.toString())
    }

    DisposableEffect(Unit) {
        onDispose {
            println("DisposableEffect C")
        }
    }
}