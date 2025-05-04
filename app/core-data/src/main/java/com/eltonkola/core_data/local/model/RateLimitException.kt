package com.eltonkola.core_data.local.model

import com.eltonkola.core_data.model.remote.models.ErrorResponse
import retrofit2.HttpException
import java.io.IOException

/**
 * Custom exception to represent Polygon API rate limit errors
 */
class RateLimitException(
    val errorResponse: ErrorResponse,
    val statusCode: Int = 429,
    message: String = errorResponse.error
) : IOException(message)

/**
 * Extension function to check if a Throwable is a rate limit exception
 */
fun Throwable.isRateLimitError(): Boolean {
    return this is RateLimitException ||
            (this is HttpException && this.code() == 429) ||
            this.message?.contains("exceeded the maximum requests", ignoreCase = true) == true
}
