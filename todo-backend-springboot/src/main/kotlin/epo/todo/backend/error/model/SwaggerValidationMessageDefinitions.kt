package epo.todo.backend.error.model

/**
 * This file contains data classes that mirror the format of
 * response messages returned by Swagger validation errors.
 */
data class SwaggerValidationMessageList(val messages: List<SwaggerValidationMessage>)

data class SwaggerValidationMessage(val key: String,
                                    val level: String,
                                    val message: String,
                                    val context: SwaggerValidationContext)

data class SwaggerValidationContext(val requestPath: String,
                                    val apiRequestContentType: String,
                                    val location: String,
                                    val requestMethod: String)
