package com.rprojects.gudangku.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthTokenFilter(
    private val jwtUtils: JwtUtils,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    private companion object {
        val logger: Logger = LoggerFactory.getLogger(AuthTokenFilter::class.java)
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            authenticateIfValidToken(request)
        } catch (ex: Exception) {
            Companion.logger.error("JWT authentication failed", ex)
        }

        filterChain.doFilter(request, response)
    }

    private fun authenticateIfValidToken(request: HttpServletRequest) {
        if (SecurityContextHolder.getContext().authentication != null) {
            return
        }

        getValidatedToken(request)?.let { token ->
            authenticateUser(token, request)
        }
    }

    private fun getValidatedToken(request: HttpServletRequest): String? =
        jwtUtils.getJwtToken(request)
            ?.takeIf(jwtUtils::validateToken)

    private fun authenticateUser(token: String, request: HttpServletRequest) {
        jwtUtils.getUsernameFromToken(token)?.let { username ->
            val userDetails = userDetailsService.loadUserByUsername(username)
            val authentication = createAuthentication(userDetails, request)

            SecurityContextHolder.getContext().authentication = authentication
            Companion.logger.debug("Authenticated user '{}' via JWT", username)
        }
    }

    private fun createAuthentication(
        userDetails: UserDetails,
        request: HttpServletRequest
    ): UsernamePasswordAuthenticationToken =
        UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.authorities
        ).apply {
            details = WebAuthenticationDetailsSource().buildDetails(request)
        }
}