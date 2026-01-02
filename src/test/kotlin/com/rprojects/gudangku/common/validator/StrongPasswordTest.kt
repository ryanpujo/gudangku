package com.rprojects.gudangku.common.validator

import com.rprojects.gudangku.common.phone.PhoneNumber
import com.rprojects.gudangku.credential.dto.CredentialDto
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StrongPasswordTest {

    private lateinit var validator: Validator

    @BeforeEach
    fun setup() {
        validator = Validation.buildDefaultValidatorFactory().validator
    }

    @Test
    fun `test invalid password with no uppercase`() {
        val dto = CredentialDto(
            password = "hello1&",
            username = "pujo",
            email = "ryanpujo@gmail.com",
            phoneNumber = PhoneNumber("85179812246", "+62"),
        )

        val violations = validator.validate(dto)

        assertEquals("password must contain at least 1 uppercase letter", violations.joinToString { it.message })
    }

    @Test
    fun `test invalid password with no digit`() {
        val dto = CredentialDto(
            password = "helLo$",
            username = "pujo",
            email = "ryanpujo@gmail.com",
            phoneNumber = PhoneNumber("85179812246", "+62"),
        )

        val violations = validator.validate(dto)

        assertEquals("password must contain at least 1 digit", violations.joinToString { it.message })
    }

    @Test
    fun `test invalid password with no special character`() {
        val dto = CredentialDto(
            password = "helLo3",
            username = "pujo",
            email = "ryanpujo@gmail.com",
            phoneNumber = PhoneNumber("85179812246", "+62"),
        )

        val violations = validator.validate(dto)

        assertEquals("Password must contain special character", violations.joinToString { it.message })
    }

    @Test
    fun `test valid password`() {
        val dto = CredentialDto(
            password = "helLo3%",
            username = "pujo",
            email = "ryanpujo@gmail.com",
            phoneNumber = PhoneNumber("85179812246", "+62"),
        )

        val violations = validator.validate(dto)

        assertTrue(violations.isEmpty())
    }
}