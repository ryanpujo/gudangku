package com.rprojects.gudangku.credential.mappers

import com.rprojects.gudangku.common.phone.PhoneNumber
import com.rprojects.gudangku.credential.dto.CredentialDto
import com.rprojects.gudangku.credential.entity.Credential
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface CredentialMapper {

    fun toCredential(dto: CredentialDto): Credential


    fun toDto(credential: Credential): CredentialDto

    fun map(phoneNumber: PhoneNumber): String {
        if (phoneNumber.number.isBlank()) return ""

        return phoneNumber.normalized
    }

    fun map(phoneNumber: String): PhoneNumber {
        val regex = Regex("""^(\(\+\d{1,3}\))([1-9]{3}[0-9]{3,4}[0-9]{4,5})$""")
        val match = regex.find(phoneNumber) ?:
            throw IllegalArgumentException("invalid phone number: $phoneNumber")

        val countryCode = match.groupValues[1].substring(1, match.groupValues[1].indexOf(')'))

        return PhoneNumber(
            countryCode = countryCode,
            number = match.groupValues[2]
        )
    }
}