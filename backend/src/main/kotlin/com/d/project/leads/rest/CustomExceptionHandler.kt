package com.d.project.leads.rest

import com.d.project.leads.exception.NotFoundException
import com.d.project.leads.exception.ValidationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {
    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(ex: ValidationException): ResponseEntity<RestResponse<String>> {
        return ResponseEntity(
            RestResponse(
                message = ex.message ?: "Validation error.",
                errors = ex.errors
            ),
            HttpHeaders(),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<RestResponse<String>> {
        return ResponseEntity(
            RestResponse(message = ex.message ?: "Not found."),
            HttpHeaders(),
            HttpStatus.NOT_FOUND
        )
    }
}
