package com.obabichevurlshortener.urlshortenerbackend.zoo

import com.obabichevurlshortener.urlshortenerbackend.UrlShortenerConfig
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.framework.recipes.shared.SharedCount
import org.apache.curator.retry.ExponentialBackoffRetry
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class RangeProvider {

    @Autowired
    @Qualifier("UrlShortenerConfig")
    lateinit var config: UrlShortenerConfig

    lateinit var client: CuratorFramework
    var range: Range? = null

    val logger = LoggerFactory.getLogger(RangeProvider::class.java)

    @PostConstruct
    fun init() {
        logger.info(
            "Init RangeProvider with parameters host: ${config.zooHost}, rangeSize: ${config.zooRangeSize}, rangeNode: ${config.zooRangeNode}"
        )
        client = CuratorFrameworkFactory.newClient(
            config.zooHost,
            ExponentialBackoffRetry(1000, 3)
        )
        client.start()
    }

    fun getNextRange(): Range {
        val counter = SharedCount(client, config.zooRangeNode, 0)

        var value = counter.versionedValue
        try {
            counter.start()
            while (!counter.trySetCount(value, value.value + 1)) {
                value = counter.versionedValue
            }

            val start = value.value * config.zooRangeSize
            val end = start + config.zooRangeSize
            logger.info("Range from ZooKeeper was obtained: ($start, $end)")
            counter.close()
            return Range(start, end)
        } catch (e: Exception) {
            logger.error("New range was not obtained: ${e.message}")
            throw e
        }
    }

    @Synchronized
    fun getNext(): Int {
        if (range == null || range!!.isFinished()) {
            range = getNextRange()
        }
        return range!!.getNext()
    }
}