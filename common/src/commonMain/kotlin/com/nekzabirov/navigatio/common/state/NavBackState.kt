package com.nekzabirov.navigatio.common.state

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nekzabirov.navigatio.common.host.NavDestination
import com.nekzabirov.navigatio.common.argument.Bundle

internal class NavBackState(
    private val route: String,
    val destination: NavDestination,
) {
    var parent: NavBackState? = null

    val enterAnimation = destination.enterAnimation
    val exitAnimation = destination.exitAnimation

    private val bundle = Bundle().apply {
        val proceedArgs = processArguments(route.let { if (it.endsWith("/")) it else "$it/" })

        destination.args.forEach {
            if (proceedArgs[it.name] == "null" && it.argument.isNullable) {
                put(it.name, null)
            }
            if (proceedArgs.containsKey(it.name)) {
                put(it.name, it.argument.type.parse(proceedArgs[it.name]!!))
            }
            else if (it.argument.isNullable && it.argument.defaultValue != null) {
                put(it.name, it.argument.defaultValue)
            }
            else {
                error("value argument ${it.name} not found in $route")
            }
        }
    }

    private fun processArguments(input: String): Map<String, String> {
        // Get placeholder names from the template

        val keys = linkedMapOf<Int, String>()

        destination.route.split("/").forEachIndexed { index, it ->
            if (it.startsWith("{") && it.endsWith("}"))
                keys[index] = it.replaceFirst("{", "").replace("}", "")
        }

        val values = hashMapOf<String, String>()

        input.split("/").forEachIndexed { index, s ->
            if (keys.containsKey(index))
                values[keys[index]!!] = s
        }

        return values
    }

    @Composable
    operator fun invoke() = Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        destination.content(bundle)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is NavBackState)
            return false
        return route == other.route
    }

    override fun hashCode(): Int {
        var result = destination.hashCode()
        result = 31 * result + (parent?.hashCode() ?: 0)
        return result
    }
}