package com.obabichevurlshortener.urlshortenerbackend.zoo

import com.obabichevurlshortener.urlshortenerbackend.UrlShortenerConfig
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.framework.recipes.shared.SharedCount
import org.apache.curator.retry.ExponentialBackoffRetry
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class ZooSequencer {
    @Autowired
    @Qualifier("UrlShortenerConfig")
    lateinit var config: UrlShortenerConfig

    lateinit var client: CuratorFramework

    val logger: Logger = LoggerFactory.getLogger(ZooSequencer::class.java)

    @PostConstruct
    fun init() {
        client = CuratorFrameworkFactory.newClient(
            config.zooHost,
            ExponentialBackoffRetry(1000, 3)
        )
        client.start()
    }

    fun getNext(): Int {
        val counter = SharedCount(client, config.zooRangeNode, 0)

        var value = counter.versionedValue
        try {
            counter.start()
            while (!counter.trySetCount(value, value.value + 1)) {
                value = counter.versionedValue
            }

            counter.close()
            return value.value
        } catch (e: Exception) {
            logger.error("New range was not obtained: ${e.message}")
            throw e
        }
    }
}