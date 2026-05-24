package com.finance.manager.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

data class ErrorResponse(val error: String)

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleNotFound(ex: ResourceNotFoundException) =
        ResponseEntity(ErrorResponse(ex.message ?: "Not found"), HttpStatus.NOT_FOUND)

    @ExceptionHandler(DuplicateResourceException::class)
    fun handleDuplicate(ex: DuplicateResourceException) =
        ResponseEntity(ErrorResponse(ex.message ?: "Conflict"), HttpStatus.CONFLICT)

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(ex: UnauthorizedException) =
        ResponseEntity(ErrorResponse(ex.message ?: "Unauthorized"), HttpStatus.UNAUTHORIZED)

    @ExceptionHandler(ForbiddenException::class)
    fun handleForbidden(ex: ForbiddenException) =
        ResponseEntity(ErrorResponse(ex.message ?: "Forbidden"), HttpStatus.FORBIDDEN)

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequest(ex: BadRequestException) =
        ResponseEntity(ErrorResponse(ex.message ?: "Bad request"), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val message = ex.bindingResult.fieldErrors
            .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity(ErrorResponse(message), HttpStatus.BAD_REQUEST)
    }
}