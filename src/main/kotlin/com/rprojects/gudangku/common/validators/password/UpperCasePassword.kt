package com.rprojects.gudangku.common.validators.password

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [UpperCasePasswordValidator::class])
@Target(AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.FIELD
)
@Retention(AnnotationRetention.RUNTIME)
annotation class UpperCasePassword(
    val message: String = "password must contain at least 1 uppercase letter",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)