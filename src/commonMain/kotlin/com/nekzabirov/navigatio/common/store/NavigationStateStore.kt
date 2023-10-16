package com.nekzabirov.navigatio.common.store

import androidx.compose.runtime.Composable
import com.nekzabirov.navigatio.common.state.NavigationController

public data class Destructible(
    public val value: Any?,
    public val destructor: ((Any?) -> Unit)? = null
)

public class NavigationStateStore(private val navigationController: NavigationController) {

    init {
        println("Init NavigationStateStore $this")
        navigationController.onBackStateChange = { invalidateStore() }
    }

    private var store = ArrayList<MutableMap<String, Destructible>>()

    public operator fun set(key: String, value: Destructible) {
        val currentIndex = navigationController.backStates.size
        store.getOrElse(currentIndex) {
            store.add(mutableMapOf())
        }
        store[currentIndex][key] = value
    }

    public operator fun get(key: String): Any? {
        return store.getOrNull(navigationController.backStates.size)?.get(key)?.value
    }

    private fun invalidateStore() {
        val currentIndex = navigationController.backStates.size
        if (currentIndex < store.size) {
            for (i in currentIndex + 1 until store.size) {
                store[i].values.forEach { it.destructor?.invoke(it.value) }
                store[i].clear()
            }
        }
    }

    public fun clear() {
        store.forEach { it.values.forEach { it.destructor?.invoke(it.value) } }
        store = ArrayList()
    }
}

@Composable
public inline fun <reified T : Any> navigationState(
    navigationStateStore: NavigationStateStore? = LocalNavigationStateStore.current,
    key: String = T::class.simpleName.orEmpty(),
    noinline destructor: ((T?) -> Unit)? = null,
    noinline constructor: () -> T,
): T {
    var value = navigationStateStore?.get(key)
    if (value !is T) {
        value = constructor().also {
            navigationStateStore?.set(key, Destructible(it, destructor as? ((Any?) -> Unit)))
        }
    }
    return value
}