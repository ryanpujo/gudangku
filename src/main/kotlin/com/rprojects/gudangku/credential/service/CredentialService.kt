package com.rprojects.gudangku.credential.service

import com.rprojects.gudangku.common.exception.DataAlreadyExistsException
import com.rprojects.gudangku.credential.dto.CredentialDto
import com.rprojects.gudangku.credential.entity.Credential
import com.rprojects.gudangku.credential.mappers.CredentialMapper
import com.rprojects.gudangku.credential.repository.CredentialRepo
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class CredentialService(
    private val credentialRepo: CredentialRepo,
    private val passwordEncoder: PasswordEncoder,
    private val credentialMapper: CredentialMapper
) {

    fun save(dto: CredentialDto): Credential {
        validateUniqueness(dto)

        val credential = credentialMapper.toCredential(dto)
            .withEncodedPassword(passwordEncoder)

        return credentialRepo.save(credential)
    }

    private fun validateUniqueness(dto: CredentialDto) {
        when {
            credentialRepo.existsById(dto.username) ->
                throw DataAlreadyExistsException("username already exists")

            credentialRepo.existsByEmail(dto.email) ->
                throw DataAlreadyExistsException("email already exists")

            credentialRepo.existsByPhoneNumber(dto.phoneNumber.normalized) ->
                throw DataAlreadyExistsException("phone number already exists")
        }
    }

    private fun Credential.withEncodedPassword(
        encoder: PasswordEncoder
    ): Credential = copy(password = requireNotNull(encoder.encode(password)))
}
