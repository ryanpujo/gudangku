package com.rprojects.gudangku.common.validators.password

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class ContainDigitPasswordValidator : ConstraintValidator<ContainDigitsPassword, String> {
    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext?
    ): Boolean {
        if (value.isNullOrBlank()) return false

        val digit = Regex("""\d""")

        return digit.containsMatchIn(value)
    }
}