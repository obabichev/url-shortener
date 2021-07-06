package com.obabichevurlshortener.urlshortenerbackend.utils

const val CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

class Base62 {
    companion object {
        fun encodeBase10(base10: Long): String {
            var b10 = base10
            require(b10 >= 0) { "b10 must be nonnegative" }
            var ret = ""
            while (b10 > 0) {
                ret = CHARACTERS[(b10 % 62).toInt()].toString() + ret
                b10 /= 62
            }
            return ret
        }
    }
}