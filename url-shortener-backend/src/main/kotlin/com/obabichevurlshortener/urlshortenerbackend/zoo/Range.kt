package com.obabichevurlshortener.urlshortenerbackend.zoo

import java.lang.RuntimeException

class Range(
    start: Int,
    private val end: Int
) {
    var current = start

    fun isFinished(): Boolean {
        return current == end
    }

    fun getNext(): Int {
        if (isFinished()) {
            throw RuntimeException("Range has been already finished")
        }
        return current++
    }
}