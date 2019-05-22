package com.obi.weather.ui.utils

fun <K, V : Any> MutableMap<K, V?>.toVarargArray():
        Array<out Pair<K, V>> = map { Pair(it.key, it.value!!) }.toTypedArray()