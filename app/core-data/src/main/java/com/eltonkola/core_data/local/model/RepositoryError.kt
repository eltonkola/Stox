package com.eltonkola.core_data.local.model

sealed class RepositoryError {
    object None : RepositoryError()
    data class NetworkError(val message: String) : RepositoryError()
    data class RateLimit(val message: String) : RepositoryError()
    data class GeneralError(val message: String) : RepositoryError()
}