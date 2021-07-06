package com.obabichevurlshortener.urlshortenerbackend.entity

import java.time.LocalDateTime
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.TextIndexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class ShortUrl(
    @Id
    val id: ObjectId = ObjectId.get(),

    val longUrl: String,

    @TextIndexed
    val shortUrl: String,

    val expiresAt: LocalDateTime,

    val createdAt: LocalDateTime = LocalDateTime.now(),
)
