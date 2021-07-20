package com.obabichevurlshortener.urlshortenerbackend.zoo

import com.obabichevurlshortener.urlshortenerbackend.UrlShortenerConfig
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class UrlIndexProvider(
    private val zooSequencer: ZooSequencer
) {

    @Autowired
    @Qualifier("UrlShortenerConfig")
    lateinit var config: UrlShortenerConfig

    var range: Range? = null

    val logger = LoggerFactory.getLogger(UrlIndexProvider::class.java)

    @Synchronized
    fun getNext(): Long {
        if (range == null || range!!.isFinished()) {
            val zooIndex = zooSequencer.getNext()

            val start = zooIndex * config.zooRangeSize
            val end = start + config.zooRangeSize
            logger.info("Range from ZooKeeper was obtained: ($start, $end)")
            range = Range(start, end)
        }
        return range!!.getNext()
    }
}