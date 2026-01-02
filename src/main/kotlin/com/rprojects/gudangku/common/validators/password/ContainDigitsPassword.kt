package com.rprojects.gudangku.common.validators.password

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [ContainDigitPasswordValidator::class])
@Target(AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.FIELD
)
@Retention(AnnotationRetention.RUNTIME)
annotation class ContainDigitsPassword(
    val message: String = "password must contain at least 1 digit",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
