package com.obabichevurlshortener.urlshortenerbackend.repository

import com.obabichevurlshortener.urlshortenerbackend.entity.ShortUrl
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface ShortUrlRepository : MongoRepository<ShortUrl, String> {
    fun findOneById(id: ObjectId): ShortUrl
    override fun deleteAll()
}
