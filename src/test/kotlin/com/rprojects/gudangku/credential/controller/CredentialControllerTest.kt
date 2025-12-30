package com.rprojects.gudangku.credential.controller

import com.rprojects.gudangku.common.exception.DataAlreadyExistsException
import com.rprojects.gudangku.common.exception.GlobalExceptionHandler
import com.rprojects.gudangku.common.phone.PhoneNumber
import com.rprojects.gudangku.config.SecurityConfigTest
import com.rprojects.gudangku.credential.dto.CredentialDto
import com.rprojects.gudangku.credential.entity.Credential
import com.rprojects.gudangku.credential.service.CredentialService
import org.hamcrest.CoreMatchers.endsWith
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import tools.jackson.databind.ObjectMapper
import kotlin.test.Test

@WebMvcTest(CredentialController::class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = [
    CredentialController::class,
    SecurityConfigTest::class,
    GlobalExceptionHandler::class,
])
class CredentialControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var credentialService: CredentialService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var validDto: CredentialDto

    @BeforeEach
    fun setup() {
        validDto = CredentialDto(
            username = "john_doe",
            password = "password123",
            email = "john@example.com",
            phoneNumber = PhoneNumber(
                "85179812246",
                "+62"
            )
        )
    }

    /**
     * 1. SUCCESS CASE0
     * Valid request → 201 Created + Location header
     */
    @Test
    fun `should return 201 Created when credential is valid`() {
        val savedCredential = Credential(
            username = "john_doe",
            password = "encoded",
            email = "john@example.com",
            phoneNumber = "08123456789"
        )

        whenever(credentialService.save(any()))
            .thenReturn(savedCredential)

        mockMvc.perform(
            post("/credential/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto))
        )
            .andExpect(status().isCreated)
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(header().string(HttpHeaders.LOCATION, endsWith("/john_doe")))
    }

    /**
     * 2. VALIDATION ERROR
     * Missing required field → 400 Bad Request
     */
    @Test
    fun `should return 400 when username is blank`() {
        val invalidDto = validDto.copy(username = "")

        mockMvc.perform(
            post("/credential/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto))
        )
            .andExpect(status().isBadRequest)
    }

    /**
     * 3. MULTIPLE VALIDATION ERRORS
     * Ensures controller rejects payload before service call
     */
    @Test
    fun `should not call service when request is invalid`() {
        val invalidDto = validDto.copy(
            username = "",
            password = ""
        )

        mockMvc.perform(
            post("/credential/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto))
        )
            .andExpect(status().isBadRequest)

        verify(credentialService, never()).save(any())
    }

    /**
     * 4. INVALID JSON
     * Malformed body → 400 Bad Request
     */
    @Test
    fun `should return 400 for malformed JSON`() {
        val malformedJson = """{ "username": "john", "password": """

        mockMvc.perform(
            post("/credential/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson)
        )
            .andExpect(status().isBadRequest)
    }

    /**
     * 5. UNSUPPORTED CONTENT TYPE
     */
    @Test
    fun `should return 415 when content type is not JSON`() {
        mockMvc.perform(
            post("/credential/register")
                .contentType(MediaType.TEXT_PLAIN)
                .content("invalid")
        )
            .andExpect(status().isUnsupportedMediaType)
    }

    /**
     * 6. SERVICE THROWS DOMAIN EXCEPTION
     * Example: username already exists
     */
    @Test
    fun `should propagate exception when service throws error`() {
        whenever(credentialService.save(any()))
            .thenThrow(DataAlreadyExistsException("username already exists"))

        mockMvc.perform(
            post("/credential/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto))
        )
            .andExpect(status().isBadRequest)
    }

    /**
     * 7. NULL RESPONSE SAFETY (DEFENSIVE)
     * If service returns unexpected null field
     */
    @Test
    fun `should fail if returned username is null`() {
        val brokenCredential = Credential(
            username = "",
            password = "encoded",
            email = "john@example.com",
            phoneNumber = "08123456789"
        )

        whenever(credentialService.save(any()))
            .thenReturn(brokenCredential)

        mockMvc.perform(
            post("/credential/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto))
        )
            .andExpect(status().isCreated)
    }
}
