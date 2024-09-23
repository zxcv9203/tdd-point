package io.hhplus.tdd.point.infrastructure.web.config

import io.hhplus.tdd.common.error.ErrorMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

data class ErrorResponse(
    val code: String,
    val message: String,
)

@RestControllerAdvice
class ApiControllerAdvice : ResponseEntityExceptionHandler() {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        log.error(ErrorMessage.INTERNAL_SERVER_ERROR.message, e)
        return ResponseEntity(
            ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value().toString(),
                ErrorMessage.INTERNAL_SERVER_ERROR.message,
            ),
            HttpStatus.INTERNAL_SERVER_ERROR,
        )
    }
}
