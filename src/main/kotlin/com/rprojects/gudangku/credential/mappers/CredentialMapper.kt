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

    fun map(phoneNumber: PhoneNumber): String =
        phoneNumber.normalized

    fun map(phoneNumber: String): PhoneNumber {
        val regex = Regex("""(\+\d{1,3})(\d+)""")
        val match = regex.find(phoneNumber)
            ?: throw IllegalArgumentException("Invalid phone number format: $phoneNumber")

        return PhoneNumber(
            countryCode = match.groupValues[1],
            number = match.groupValues[2]
        )
    }
}