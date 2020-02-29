package com.example.spotguide.core.extension

fun <T> MutableList<T>.removeIfContains(element: T) {
    if (this.contains(element)) this.remove(element)
}

fun <T> MutableList<T>.addIfNotContain(element: T) {
    if (!this.contains(element)) this.add(element)
}

fun <T> MutableList<T>.addIfNotContain(elements: List<T>) {
    elements.forEach { addIfNotContain(it) }
}