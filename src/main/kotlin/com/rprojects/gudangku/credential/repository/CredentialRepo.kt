package com.rprojects.gudangku.credential.repository

import com.rprojects.gudangku.credential.entity.Credential
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CredentialRepo : JpaRepository<Credential, String> {
    fun existsByEmail(email: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean
}