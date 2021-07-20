package com.obabichevurlshortener.urlshortenerbackend

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration("UrlShortenerConfig")
@ConfigurationProperties(prefix = "application.params")
class UrlShortenerConfig(
    var shortUrlPrefix: String = "",
    var zooHost: String = "",
    var zooRangeNode: String = "",
    var zooRangeSize: Int = 0
)
