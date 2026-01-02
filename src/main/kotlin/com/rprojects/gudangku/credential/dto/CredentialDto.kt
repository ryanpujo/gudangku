package com.rprojects.gudangku.credential.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.rprojects.gudangku.common.phone.PhoneNumber
import com.rprojects.gudangku.common.validators.password.StrongPassword
import com.rprojects.gudangku.credential.entity.AccountStatus
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CredentialDto(
    @field:NotBlank(message = "username is required")
    val username: String,

    @field:NotBlank(message = "password is required")
    @field:Size(min = 5, message = "password must be at least 5 characters")
    @field:StrongPassword
    val password: String,

    @field:NotBlank(message = "email is required")
    @field:Email(message = "must have a valid email")
    val email: String,

    @field:Valid
    @JsonProperty("phone_number")
    val phoneNumber: PhoneNumber,

    val accountStatus: AccountStatus = AccountStatus.DISABLED
)
