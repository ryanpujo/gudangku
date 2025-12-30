package com.rprojects.gudangku.common.validator

import com.rprojects.gudangku.common.phone.PhoneNumber
import com.rprojects.gudangku.common.validators.PhoneValidator
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class PhoneValidatorTest {

    private lateinit var validator: PhoneValidator

    private lateinit var phoneNumber: PhoneNumber

    @BeforeEach
    fun setup() {
        validator = PhoneValidator()
    }

    @Test
    fun `test valid phone number`() {
        phoneNumber = PhoneNumber(
            number = "85179812246",
            countryCode = "+62"
        )
        assertTrue(validator.isValid(phoneNumber.number, null))
    }

    @Test
    fun `test valid phone number with 10 digits`() {
        phoneNumber = PhoneNumber(
            number = "8517981224",
            countryCode = "+62"
        )
        assertTrue(validator.isValid(phoneNumber.number, null))
    }

    @Test
    fun `test valid phone number with 13 digits`() {
        phoneNumber = PhoneNumber(
            number = "851798122465",
            countryCode = "+62"
        )
        assertTrue(validator.isValid(phoneNumber.number, null))
    }

    @Test
    fun `test invalid phone number with dash`() {
        phoneNumber = PhoneNumber(
            number = "851-7981-2246",
            countryCode = "+62"
        )
        assertFalse(validator.isValid(phoneNumber.number, null))
    }

    @Test
    fun `test invalid phone number start with 0`() {
        phoneNumber = PhoneNumber(
            number = "085179812246",
            countryCode = "+62"
        )
        assertFalse(validator.isValid(phoneNumber.number, null))
    }

    @Test
    fun `test invalid phone number less than 10 digits`() {
        phoneNumber = PhoneNumber(
            number = "851798122",
            countryCode = "+62"
        )
        assertFalse(validator.isValid(phoneNumber.number, null))
    }

    @Test
    fun `test invalid phone number more than 12 digits`() {
        phoneNumber = PhoneNumber(
            number = "8517981224677",
            countryCode = "+62"
        )
        assertFalse(validator.isValid(phoneNumber.number, null))
    }
}