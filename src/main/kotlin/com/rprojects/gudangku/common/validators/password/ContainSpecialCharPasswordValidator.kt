package com.rprojects.gudangku.common.validators.password

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class ContainSpecialCharPasswordValidator : ConstraintValidator<ContainSpecialCharPassword, String> {
    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext?
    ): Boolean {
        if (value.isNullOrBlank()) return false

        val specialChar = Regex("""\W""")
        return specialChar.containsMatchIn(value)
    }
}