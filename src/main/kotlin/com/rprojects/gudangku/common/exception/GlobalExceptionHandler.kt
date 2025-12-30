package com.rprojects.gudangku.common.exception

import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException
    ): ResponseEntity<ErrorResponse> {

        val errors: Map<String, String> =
            ex.bindingResult.allErrors
                .filter { it.defaultMessage != null }
                .associate { error ->
                    when (error) {
                        is FieldError -> error.field to error.defaultMessage!!
                        else -> error.objectName to error.defaultMessage!!
                    }
                }

        return ResponseEntity
            .badRequest()
            .body(
                ErrorResponse(
                    message = "validation errors",
                    details = errors
                )
            )
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(
        ex: EntityNotFoundException
    ): ResponseEntity<ErrorResponse> {

        val message = ex.message ?: "Entity not found"

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                ErrorResponse(
                    message = message,
                    details = mapOf("error" to message)
                )
            )
    }

    @ExceptionHandler(DataAlreadyExistsException::class)
    fun handleDataAlreadyExistsException(exception: DataAlreadyExistsException): ResponseEntity<ErrorResponse> {
        val message = exception.message ?: "Data already exists"

        return ResponseEntity.badRequest().body(
            ErrorResponse(
                message = message,
                details = mapOf("error" to message)
            )
        )
    }

}