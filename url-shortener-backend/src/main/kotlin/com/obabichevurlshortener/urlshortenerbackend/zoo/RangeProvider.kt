package com.obabichevurlshortener.urlshortenerbackend.zoo

import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.framework.recipes.shared.SharedCount
import org.apache.curator.retry.ExponentialBackoffRetry
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

const val RANGE_SIZE = 10

@Component
class RangeProvider {

    lateinit var client: CuratorFramework
    var range: Range? = null

    val logger = LoggerFactory.getLogger(RangeProvider::class.java)

    @PostConstruct
    fun init() {
        client = CuratorFrameworkFactory.newClient(
            "zoo:2181",
            ExponentialBackoffRetry(1000, 3)
        )
        client.start()
    }

    fun getNextRange(): Range {
        val counter = SharedCount(client, "/range", 0)

        var value = counter.versionedValue
        try {
            counter.start()
            while (!counter.trySetCount(value, value.value + 1)) {
                value = counter.versionedValue
            }

            val start = value.value * RANGE_SIZE
            val end = start + RANGE_SIZE
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