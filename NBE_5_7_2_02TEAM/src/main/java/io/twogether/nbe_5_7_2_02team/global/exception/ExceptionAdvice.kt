package io.twogether.nbe_5_7_2_02team.global.exception

import io.twogether.nbe_5_7_2_02team.global.response.error.ErrorResponse
import io.twogether.nbe_5_7_2_02team.global.response.error.ErrorStatus
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.method.HandlerMethod

@ControllerAdvice
class ExceptionAdvice {
    private val log = LoggerFactory.getLogger(ExceptionAdvice::class.java)

    @ExceptionHandler(ErrorException::class)
    @ResponseBody
    fun handleException(
        e: ErrorException,
        handlerMethod: HandlerMethod?,
    ): ResponseEntity<ErrorResponse<Any?>> {
        val errorCode = e.errorCode
        val httpStatus = errorCode.errorStatus.toHttpStatus()

        log.error("[ExceptionAdvice] ${errorCode.code}, ${errorCode.message}", e)

        return ResponseEntity
            .status(httpStatus)
            .body(ErrorResponse(errorCode.code, errorCode.message))
    }

    private fun ErrorStatus.toHttpStatus(): HttpStatus =
        when (this) {
            ErrorStatus.BAD_REQUEST -> HttpStatus.BAD_REQUEST
            ErrorStatus.NOT_FOUND -> HttpStatus.NOT_FOUND
            ErrorStatus.CONFLICT -> HttpStatus.CONFLICT
            ErrorStatus.UNAUTHORIZED -> HttpStatus.UNAUTHORIZED
            ErrorStatus.FORBIDDEN -> HttpStatus.FORBIDDEN
        }
}
