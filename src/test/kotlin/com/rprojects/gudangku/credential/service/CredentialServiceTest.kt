package com.rprojects.gudangku.credential.service

import com.rprojects.gudangku.common.exception.DataAlreadyExistsException
import com.rprojects.gudangku.common.phone.PhoneNumber
import com.rprojects.gudangku.credential.dto.CredentialDto
import com.rprojects.gudangku.credential.entity.Credential
import com.rprojects.gudangku.credential.mappers.CredentialMapper
import com.rprojects.gudangku.credential.repository.CredentialRepo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull


@ExtendWith(MockitoExtension::class)
class CredentialServiceTest {

    @Mock
    private lateinit var credentialRepo: CredentialRepo

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @Mock
    private lateinit var credentialMapper: CredentialMapper

    @InjectMocks
    private lateinit var credentialService: CredentialService

    private lateinit var dto: CredentialDto
    private lateinit var credential: Credential

    @BeforeEach
    fun setup() {
        dto = CredentialDto(
            username = "pujo",
            password = "plainPassword",
            email = "pujo@gmail.com",
            phoneNumber = PhoneNumber(
                "85179812246",
                "+62"
            )
        )

        credential = Credential(
            username = "pujo",
            password = "plainPassword",
            email = "pujo@gmail.com",
            phoneNumber = dto.phoneNumber.toString()
        )
    }

    @Test
    fun `should throw exception when username already exists`() {
        whenever(credentialRepo.existsById(dto.username)).thenReturn(true)

        val exception = assertFailsWith<DataAlreadyExistsException> {
            credentialService.save(dto)
        }

        assertEquals("username already exists", exception.message)
        verify(credentialRepo, never()).save(any())
    }

    @Test
    fun `should throw exception when email already exists`() {
        whenever(credentialRepo.existsById(dto.username)).thenReturn(false)
        whenever(credentialRepo.existsByEmail(dto.email)).thenReturn(true)

        val exception = assertFailsWith<DataAlreadyExistsException> {
            credentialService.save(dto)
        }

        assertEquals("email already exists", exception.message)
        verify(credentialRepo, never()).save(any())
    }

    @Test
    fun `should throw exception when phone number already exists`() {
        whenever(credentialRepo.existsById(dto.username)).thenReturn(false)
        whenever(credentialRepo.existsByEmail(dto.email)).thenReturn(false)
        whenever(credentialRepo.existsByPhoneNumber(dto.phoneNumber.toString())).thenReturn(true)

        val exception = assertFailsWith<DataAlreadyExistsException> {
            credentialService.save(dto)
        }

        assertEquals("phone number already exists", exception.message)
        verify(credentialRepo, never()).save(any())
    }

    @Test
    fun `should save credential successfully`() {
        whenever(credentialRepo.existsById(dto.username)).thenReturn(false)
        whenever(credentialRepo.existsByEmail(dto.email)).thenReturn(false)
        whenever(credentialRepo.existsByPhoneNumber(dto.phoneNumber.toString())).thenReturn(false)

        whenever(credentialMapper.toCredential(dto)).thenReturn(credential)
        whenever(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword")
        whenever(credentialRepo.save(any())).thenAnswer { it.arguments[0] }

        val result = credentialService.save(dto)

        assertNotNull(result)
        assertEquals("encodedPassword", result.password)
        assertEquals(dto.username, result.username)
        assertEquals(dto.email, result.email)
        assertEquals(dto.phoneNumber.toString(), result.phoneNumber)

        verify(credentialMapper).toCredential(dto)
        verify(passwordEncoder).encode("plainPassword")
        verify(credentialRepo).save(result)
    }
}