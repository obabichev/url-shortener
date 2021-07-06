package com.obabichevurlshortener.urlshortenerbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(
    UrlShortenerConfig::class
)
class UrlShortenerBackendApplication

fun main(args: Array<String>) {
    runApplication<UrlShortenerBackendApplication>(*args)
}
