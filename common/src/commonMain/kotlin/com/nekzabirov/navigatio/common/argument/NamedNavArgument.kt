package com.nekzabirov.navigatio.common.argument

public fun navArgument(name: String,  builder: NavArgument.Builder.() -> Unit): NamedNavArgument = NamedNavArgument(
    name = name,
    argument = NavArgument.Builder().apply(builder).build()
)

public class NamedNavArgument internal constructor(public val name: String, public val argument: NavArgument) {

}