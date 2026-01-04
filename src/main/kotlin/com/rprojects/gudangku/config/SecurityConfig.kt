package com.rprojects.gudangku.config

import com.rprojects.gudangku.jwt.AuthTokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
class SecurityConfig(
    private val authTokenFilter: AuthTokenFilter
) {


    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { csrfConfigurer -> csrfConfigurer.disable() }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
        http.authorizeHttpRequests {
            it.requestMatchers("/credential/register", "/h2-console/**", "/signing").permitAll()
                .anyRequest().authenticated()
        }

        http.sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        http.headers { it.frameOptions(HeadersConfigurer<HttpSecurity>.FrameOptionsConfig::sameOrigin) }

        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Bean
    fun authenticationManager(builder: AuthenticationConfiguration): AuthenticationManager =
        builder.authenticationManager
}