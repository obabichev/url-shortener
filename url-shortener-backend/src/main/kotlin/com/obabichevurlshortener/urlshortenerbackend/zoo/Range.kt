package com.obabichevurlshortener.urlshortenerbackend.zoo

import java.lang.RuntimeException

class Range(
    start: Long,
    private val end: Long
) {
    var current = start

    fun isFinished(): Boolean {
        return current == end
    }

    fun getNext(): Long {
        if (isFinished()) {
            throw RuntimeException("Range has been already finished")
        }
        return current++
    }
}