package io.hhplus.tdd.point.infrastructure.web.config

import io.hhplus.tdd.common.ErrorMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.ConversionNotSupportedException
import org.springframework.beans.TypeMismatchException
import org.springframework.boot.context.properties.bind.BindException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.converter.HttpMessageNotWritableException
import org.springframework.validation.method.MethodValidationException
import org.springframework.web.ErrorResponseException
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.async.AsyncRequestTimeoutException
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.resource.NoResourceFoundException

data class ErrorResponse(
    val code: String,
    val message: String,
)

@RestControllerAdvice
class ApiControllerAdvice {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        log.debug(ErrorMessage.INVALID_REQUEST.message, e)
        return ResponseEntity(
            ErrorResponse(
                HttpStatus.BAD_REQUEST.value().toString(),
                e.message ?: ErrorMessage.INVALID_REQUEST.message,
            ),
            HttpStatus.BAD_REQUEST,
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        log.debug(ErrorMessage.INVALID_REQUEST.message, e)

        // HttpMessageNotReadableException의 경우 발생 원인 메시지가 변경되기 때문에
        // 메시지에서 "problem:" 뒤에 오는 메시지 부분만 추출 (JSON parse error 메시지 무시)
        val message =
            if (e.rootCause is IllegalArgumentException) {
                e.message?.substringAfter("problem: ") ?: ErrorMessage.INVALID_REQUEST.message
            } else {
                ErrorMessage.INVALID_REQUEST.message
            }

        return ResponseEntity(
            ErrorResponse(
                HttpStatus.BAD_REQUEST.value().toString(),
                message,
            ),
            HttpStatus.BAD_REQUEST,
        )
    }

    @ExceptionHandler(
        HttpRequestMethodNotSupportedException::class,
        HttpMediaTypeNotSupportedException::class,
        HttpMediaTypeNotAcceptableException::class,
        MissingPathVariableException::class,
        MissingServletRequestParameterException::class,
        MissingServletRequestPartException::class,
        ServletRequestBindingException::class,
        MethodArgumentNotValidException::class,
        HandlerMethodValidationException::class,
        NoHandlerFoundException::class,
        NoResourceFoundException::class,
        AsyncRequestTimeoutException::class,
        ErrorResponseException::class,
        MaxUploadSizeExceededException::class,
        ConversionNotSupportedException::class,
        TypeMismatchException::class,
        HttpMessageNotWritableException::class,
        MethodValidationException::class,
        BindException::class,
    )
    fun handleBadRequestException(e: Exception): ResponseEntity<ErrorResponse> {
        log.debug(ErrorMessage.INVALID_REQUEST.message, e)
        return ResponseEntity(
            ErrorResponse(
                HttpStatus.BAD_REQUEST.value().toString(),
                ErrorMessage.INVALID_REQUEST.message,
            ),
            HttpStatus.BAD_REQUEST,
        )
    }

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
