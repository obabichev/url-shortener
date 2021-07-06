package com.obabichevurlshortener.urlshortenerbackend

import com.obabichevurlshortener.urlshortenerbackend.entity.ShortUrl
import com.obabichevurlshortener.urlshortenerbackend.repository.ShortUrlRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UrlShortenerController(
    private val shortUrlRepository: ShortUrlRepository
) {

    @GetMapping("/")
    fun getAllUrls(): ResponseEntity<MutableList<ShortUrl>> {
        val urls = shortUrlRepository.findAll()
        return ResponseEntity.ok(urls)
    }

}