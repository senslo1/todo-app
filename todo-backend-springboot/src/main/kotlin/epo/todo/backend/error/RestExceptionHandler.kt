package epo.todo.backend.error

import com.atlassian.oai.validator.springmvc.InvalidRequestException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import epo.todo.backend.error.exception.BadRequestException
import epo.todo.backend.error.exception.NotFoundException
import epo.todo.backend.error.model.ApiError
import epo.todo.backend.error.model.SwaggerValidationMessageList
import mu.KotlinLogging
import org.slf4j.MDC
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.UUID

/**
 * Class that intercepts exceptions thrown by code called by REST controllers.
 * Builds custom response bodies of type [ApiError] and adds an appropriate http code.
 */
@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {

    private val log = KotlinLogging.logger {}

    /**
     * Extension function joining all stack trace elements to a single string
     * for simple stack trace logging.
     */
    fun java.lang.Exception.prettyStackTrace(): String =
            this.stackTrace.asList().joinToString(separator = "\nat ")

    private fun logException(ex: Exception) {
        log.error { ex.message }
        log.error { ex.prettyStackTrace() }
    }

    private fun buildResponse(status: HttpStatus,
                              message: String?,
                              debugMessage: SwaggerValidationMessageList? = null): ResponseEntity<ApiError> {
        val apiError = ApiError(status = "${status.value()} ${status.reasonPhrase}",
                                message = message ?: UNEXPECTED_ERROR,
                                debugMessage = debugMessage,
                                correlationId = UUID.fromString(MDC.get(CORRELATION_ID_LOG_VAR_NAME)))
        return ResponseEntity(apiError, status)
    }

    @ExceptionHandler(value = [BadRequestException::class])
    fun handleException(ex: BadRequestException): ResponseEntity<ApiError> {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.message)
                .also { logException(ex) }
    }

    @ExceptionHandler(value = [NotFoundException::class])
    fun handleException(ex: NotFoundException): ResponseEntity<ApiError> {
        return buildResponse(HttpStatus.NOT_FOUND, ex.message)
                .also { logException(ex) }
    }

    @ExceptionHandler(value = [InvalidRequestException::class])
    fun handleException(ex: InvalidRequestException, req: WebRequest): ResponseEntity<ApiError> {
        val debugMessage = jacksonObjectMapper().readValue<SwaggerValidationMessageList>(ex.message!!)
        val message = "The request did not follow the constraints given by the OpenApi specification. " +
                "Check the debug message for more details."
        return buildResponse(HttpStatus.BAD_REQUEST, message, debugMessage)
                .also { logException(ex) }
    }

    @ExceptionHandler(value = [Exception::class])
    fun handleException(ex: Exception): ResponseEntity<ApiError> {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.message)
                .also { logException(ex) }
    }

    companion object {
        private const val CORRELATION_ID_LOG_VAR_NAME = "correlationId"
        private const val UNEXPECTED_ERROR = "Unexpected error"
    }
}
