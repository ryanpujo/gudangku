package com.rprojects.gudangku.jwt

import com.rprojects.gudangku.common.TimeUtils
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtils(
    private val jwtProperties: JwtProperties
) {
    private companion object {
        private const val BEARER_PREFIX = "Bearer "
        private const val TOKEN_DURATION_MINUTES = 5L

        val logger: Logger = LoggerFactory.getLogger(JwtUtils::class.java)
    }

    fun getJwtToken(request: HttpServletRequest?): String? =
        request
            ?.getHeader("Authorization")
            ?.takeIf { it.startsWith(BEARER_PREFIX) }
            ?.removePrefix(BEARER_PREFIX)
            ?.trim()

    fun generateToken(userDetails: UserDetails): String =
        Jwts.builder()
            .subject(userDetails.username)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + TimeUtils.minute * TOKEN_DURATION_MINUTES))
            .signWith(key())
            .compact()

    fun getUsernameFromToken(token: String): String? =
        Jwts.parser()
            .verifyWith(key())
            .build()
            .parseSignedClaims(token)
            .payload
            .subject

    fun validateToken(token: String): Boolean = try {
        Jwts.parser()
            .verifyWith(key())
            .build()
            .parseSignedClaims(token)
        true
    } catch (exception: Exception) {
        when (exception) {
            is MalformedJwtException -> logger.error("Invalid JWT token", exception)
            is ExpiredJwtException -> logger.error("JWT token is expired", exception)
            is UnsupportedJwtException -> logger.error("JWT token is unsupported", exception)
            is IllegalArgumentException -> logger.error("JWT claims string is empty", exception)
            else -> logger.error("Unexpected JWT validation error", exception)
        }
        false
    }

    private fun key(): SecretKey =
        Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secret))
}