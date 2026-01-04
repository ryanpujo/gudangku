package com.rprojects.gudangku.jwt

import com.rprojects.gudangku.credential.service.CredentialService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthTokenFilter(
    private val jwtUtils: JwtUtils,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    companion object {
        private val logger = LoggerFactory.getLogger(AuthTokenFilter::class.java)
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            processJwtAuthentication(request)
        } catch (ex: Exception) {
            Companion.logger.error("JWT authentication failed", ex)
        }

        filterChain.doFilter(request, response)
    }

    private fun processJwtAuthentication(request: HttpServletRequest) {
        // Avoid overriding existing authentication
        if (SecurityContextHolder.getContext().authentication != null) {
            return
        }

        val token = jwtUtils.getJwtToken(request)
            ?.takeIf(jwtUtils::validateToken)
            ?: return

        val username = jwtUtils.getUsernameFromToken(token) ?: ""
        val userDetails = userDetailsService.loadUserByUsername(username)

        val authentication = UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.authorities
        ).apply {
            details = WebAuthenticationDetailsSource().buildDetails(request)
        }

        SecurityContextHolder.getContext().authentication = authentication

        Companion.logger.debug("Authenticated user '{}' via JWT", username)
    }
}
