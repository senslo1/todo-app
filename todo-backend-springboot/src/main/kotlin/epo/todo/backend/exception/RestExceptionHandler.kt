package epo.todo.backend.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * Class that intercepts exceptions thrown by code called by REST controllers.
 * Builds custom response bodies of type [ApiError] and adds an appropriate http code.
 */
@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [BadRequestException::class])
    fun handleException(ex: BadRequestException): ResponseEntity<ApiError> {
        val apiError = ApiError(HttpStatus.BAD_REQUEST, ex.message!!)
        return ResponseEntity(apiError, apiError.status)
    }

    @ExceptionHandler(value = [NotFoundException::class])
    fun handleException(ex: NotFoundException): ResponseEntity<ApiError> {
        val apiError = ApiError(HttpStatus.NOT_FOUND, ex.message!!)
        return ResponseEntity(apiError, apiError.status)
    }

    @ExceptionHandler(value = [Exception::class])
    fun handleException(ex: Exception): ResponseEntity<ApiError> {
        val apiError = ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.message!!)
        return ResponseEntity(apiError, apiError.status)
    }
}
