package com.rprojects.gudangku.common.validators.password

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class UpperCasePasswordValidator : ConstraintValidator<UpperCasePassword, String> {

    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext?
    ): Boolean {
        if (value.isNullOrBlank()) return false
        val upperCase = Regex("""[A-Z]""")
        print(upperCase.containsMatchIn(value))
        return upperCase.containsMatchIn(value)
    }
}