package com.nekzabirov.navigatio.common.argument

public abstract class NavType<T> private constructor() {
    public abstract fun parse(value: Any): T

    public abstract fun parse(value: String): T

    public companion object {
        public val StringType: NavType<String> = object : NavType<String>() {
            override fun parse(value: Any): String {
                return value.toString()
            }

            override fun parse(value: String): String {
                return value
            }
        }

        public val IntType: NavType<Int> = object : NavType<Int>() {
            override fun parse(value: Any): Int {
                return value as Int
            }

            override fun parse(value: String): Int {
                return value.toInt()
            }
        }

        public val LongType: NavType<Long> = object : NavType<Long>() {
            override fun parse(value: Any): Long {
                return value as Long
            }

            override fun parse(value: String): Long {
                return value.toLong()
            }
        }

        public val BoolType: NavType<Boolean> = object : NavType<Boolean>() {
            override fun parse(value: Any): Boolean {
                return value as Boolean
            }

            override fun parse(value: String): Boolean {
                return value.toBoolean()
            }
        }
    }
}