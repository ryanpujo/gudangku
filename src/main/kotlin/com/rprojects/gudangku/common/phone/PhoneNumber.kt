package com.rprojects.gudangku.common.phone

import com.fasterxml.jackson.annotation.JsonProperty
import com.rprojects.gudangku.common.validators.ValidPhone
import jakarta.persistence.Transient
import jakarta.validation.constraints.NotBlank

data class PhoneNumber(
    @field:NotBlank(message = "Phone number is required")
    @field:ValidPhone(message = "phone number should only contain numbers without country code and 0")
    val number: String,

    @JsonProperty("country_code")
    @field:NotBlank(message = "country code is required")
    val countryCode: String
) {
    @get:Transient
    val normalized: String get() {

        return "$countryCode$number"
    }
}
