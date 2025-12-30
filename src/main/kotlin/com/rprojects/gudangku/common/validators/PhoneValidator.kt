package com.rprojects.gudangku.common.validators

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class PhoneValidator : ConstraintValidator<ValidPhone, String> {
    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext?
    ): Boolean {
        if (value.isNullOrBlank()) return false

        return value.matches(Regex("^[1-9]{3}[0-9]{3,4}[0-9]{4,5}\$"))
    }
}