package com.rprojects.gudangku.jwt

import com.rprojects.gudangku.common.TimeUtils
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey


@Component
class JwtUtils(
    private val jwtProperties: JwtProperties
) {

    companion object {
        private val logger = LoggerFactory.getLogger(JwtUtils::class.java)
    }

    @Value("{spring.app.jwtSecret}")
    private lateinit var jwtSecret: String

    fun getJwtToken(req: HttpServletRequest?): String? {
        val bearerToken = req?.getHeader("Authorization")

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return null
        }
        return bearerToken.substring("Bearer ".length).trim()
    }

    fun generateToken(userDetails: UserDetails): String? {
        val username = userDetails.username

        return Jwts.builder()
            .subject(username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + TimeUtils.minute*5))
            .signWith(key())
            .compact()
    }

    fun getUsernameFromToken(token: String): String? {
        return Jwts.parser().verifyWith(key())
            .build().parseSignedClaims(token)
            .payload.subject ?: return null
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser().verifyWith(key()).build().parseSignedClaims(token)
            return true
        } catch (e: MalformedJwtException) {
            logger.error("Invalid JWT token: {}", e.message)
        } catch (e: ExpiredJwtException) {
            logger.error("JWT token is expired: {}", e.message)
        } catch (e: UnsupportedJwtException) {
            logger.error("JWT token is unsupported: {}", e.message)
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty: {}", e.message)
        }
        return false
    }

    private fun key(): SecretKey? {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secret))
    }
}