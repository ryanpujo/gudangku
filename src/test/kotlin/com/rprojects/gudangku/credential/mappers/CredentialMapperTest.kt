package com.rprojects.gudangku.credential.mappers

import com.rprojects.gudangku.common.phone.PhoneNumber
import com.rprojects.gudangku.credential.dto.CredentialDto
import com.rprojects.gudangku.credential.entity.Credential
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mapstruct.factory.Mappers
import kotlin.test.assertEquals


class CredentialMapperTest {

    private lateinit var credential: Credential
    private lateinit var dto: CredentialDto

    private val credentialMapper: CredentialMapper =
        Mappers.getMapper(CredentialMapper::class.java)

    @BeforeEach
    fun setup() {
        credential = Credential(
            username = "ryanpujo",
            password = "okeoke",
            email = "ryanpuj0@gmail.com",
            phoneNumber = "(+62)85179812246"
        )

        dto = CredentialDto(
            username = "ryanpujo",
            password = "okeoke",
            email = "ryanpuj0@gmail.com",
            phoneNumber = PhoneNumber(
                "85179812246",
                "+62"
            )
        )
    }

    /* =========================
       Happy Path Tests
       ========================= */

    @Test
    fun `should map Credential to CredentialDto`() {
        val result = credentialMapper.toDto(credential)

        assertEquals(dto, result)
    }

    @Test
    fun `should map CredentialDto to Credential`() {
        val result = credentialMapper.toCredential(dto)

        assertEquals(credential, result)
    }

    /* =========================
       Empty String Handling
       ========================= */

    @Test
    fun `should preserve empty strings`() {
        val emptyDto = CredentialDto(
            username = "",
            password = "",
            email = "",
            phoneNumber = PhoneNumber("","")
        )

        val result = credentialMapper.toCredential(emptyDto)

        assertEquals("", result.username)
        assertEquals("", result.password)
        assertEquals("", result.email)
        assertEquals("", result.phoneNumber)
    }

    /* =========================
       Defensive Copy Check
       ========================= */

//    @Test
//    fun `should create new instances and not reuse references`() {
//        val result = credentialMapper.toDto(credential)
//
//        assertNotSame(credential, result)
//    }
}
