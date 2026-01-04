package com.rprojects.gudangku.Security

import com.rprojects.gudangku.credential.repository.CredentialRepo
import com.rprojects.gudangku.credential.service.CredentialService
import jakarta.persistence.EntityNotFoundException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.Collections
import kotlin.jvm.optionals.getOrElse

@Service
class CredentialDetailService(
    private val credentialRepo: CredentialRepo
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {

        val user = credentialRepo.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("Username $username not found") }

        return User(
            username,
            user.password,
            Collections.unmodifiableList<GrantedAuthority>(listOf(SimpleGrantedAuthority("ROLE_USER")))
        )
    }
}