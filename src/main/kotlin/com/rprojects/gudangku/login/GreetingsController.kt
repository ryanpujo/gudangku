package com.rprojects.gudangku.login

import com.rprojects.gudangku.jwt.JwtUtils
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class GreetingsController(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtils: JwtUtils
) {

    companion object {
        val logger = LoggerFactory.getLogger(GreetingsController::class.java)!!
    }

    @GetMapping("/hello")
    fun hello(): String {
        return "Hello World!"
    }

    @PostMapping("/signing")
    fun login(@RequestBody loginReq: LoginReq): ResponseEntity<String> {

        try {
            logger.info("start authenticating server")
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    loginReq.username,
                    loginReq.password
                )
            )
            SecurityContextHolder.getContext().authentication = authentication
            val userDetails = authentication.principal as UserDetails
            val token = jwtUtils.generateToken(userDetails)
            return ResponseEntity.ok(token)
        } catch (e: AuthenticationException) {
            return ResponseEntity.badRequest().body(e.message)
        }

    }
}