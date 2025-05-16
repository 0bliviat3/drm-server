package com.core.drm.crypto.util

fun <T> partition(list: List<T>, size: Int): List<List<T>> {
    return list.chunked(size)
}