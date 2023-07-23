package com.nekzabirov.navigatio.common.argument

public data class NavArgument internal constructor(
    val type: NavType<*>,
    val isNullable: Boolean,
    val defaultValue: Any?
) {
    public class Builder {
        public var type: NavType<*>? = null
        public var isNullable: Boolean = false
        public var defaultValue: Any? = null

        internal fun build() = NavArgument(
            type = type ?: NavType.StringType,
            isNullable = isNullable,
            defaultValue = defaultValue
        )
    }
}