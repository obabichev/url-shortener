package com.obabichevurlshortener.urlshortenerbackend

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class ShortUrlService {

    @Autowired
    @Qualifier("UrlShortenerConfig")
    lateinit var config: UrlShortenerConfig

    fun shortUrlByKey(key: String): String {
        return "${config.shortUrlPrefix}/$key"
    }

}