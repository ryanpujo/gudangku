package com.rprojects.gudangku.common.validators.password

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [])
@UpperCasePassword
@ContainDigitsPassword
@ContainSpecialCharPassword
annotation class StrongPassword(
    val message: String = "password is invalid",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
