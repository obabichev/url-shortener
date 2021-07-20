package com.obabichevurlshortener.urlshortenerbackend

import com.obabichevurlshortener.urlshortenerbackend.dto.CreateShortUrlRequest
import com.obabichevurlshortener.urlshortenerbackend.entity.ShortUrl
import com.obabichevurlshortener.urlshortenerbackend.repository.ShortUrlRepository
import com.obabichevurlshortener.urlshortenerbackend.utils.Base62
import com.obabichevurlshortener.urlshortenerbackend.zoo.RangeProvider
import java.time.LocalDateTime
import javax.servlet.http.HttpServletResponse
import kotlin.math.abs
import kotlin.random.Random
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UrlShortenerController(
    private val shortUrlRepository: ShortUrlRepository,
    private val shortUrlService: ShortUrlService,
    private val rangeProvider: RangeProvider
) {

    var logger: Logger = LoggerFactory.getLogger(UrlShortenerController::class.java)

    @GetMapping("/short-url")
    fun getAllUrls(): ResponseEntity<MutableList<ShortUrl>> {
        val urls = shortUrlRepository.findAll()
        return ResponseEntity.ok(urls)
    }

    @PostMapping("/short-url")
    fun createShortUrl(@RequestBody body: CreateShortUrlRequest): ResponseEntity<ShortUrl> {

        val nextId = rangeProvider.getNext()
        logger.info("[DEV] next id: $nextId")

        val shortUrl = shortUrlRepository.save(
            ShortUrl(
                longUrl = body.longUrl,
                shortUrl = shortUrlService.shortUrlByKey(Base62.encodeBase10(abs(Random.nextLong()))),
                expiresAt = LocalDateTime.now().plusMonths(12)
            )
        )

        return ResponseEntity.ok(shortUrl)
    }

    @GetMapping("/su/{key}")
    fun shortUrlRedirect(response: HttpServletResponse, @PathVariable key: String) {
        logger.info("Short url redirect with key $key")

        val shortUrl = shortUrlRepository.findOneByShortUrl(shortUrlService.shortUrlByKey(key))

        response.sendRedirect(shortUrl.longUrl)
    }
}
