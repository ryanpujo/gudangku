package com.rprojects.gudangku.credential.repository

import com.rprojects.gudangku.credential.entity.Credential
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface CredentialRepo : JpaRepository<Credential, String> {
    fun existsByEmail(email: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean
//    fun findByEmail(email: String): Optional<Credential>
//    fun findByPhoneNumber(phoneNumber: String): Optional<Credential>
    fun findByUsername(username: String): Optional<Credential>
}