package com.nekzabirov.navigatio.common.argument

public class Bundle {
    private val data = hashMapOf<String, Any?>()

    public fun put(key: String, value: Any?) {
        data[key] = value
    }

    public fun getString(key: String): String? = data[key] as String

    public fun getInt(key: String): Int? = data[key] as? Int

    public fun getLong(key: String): Long? = data[key] as? Long

    public fun getBool(key: String): Boolean? = data[key] as? Boolean
}