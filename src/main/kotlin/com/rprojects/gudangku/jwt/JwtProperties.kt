package com.rprojects.gudangku.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import javax.crypto.SecretKey

@Component
@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    var secret: String = "",
)
