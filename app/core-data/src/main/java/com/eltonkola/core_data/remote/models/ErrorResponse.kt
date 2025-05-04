package com.eltonkola.core_data.model.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Model for the error response from Polygon API
 */
data class ErrorResponse(
    @SerializedName("status") val status: String,
    @SerializedName("request_id") val requestId: String,
    @SerializedName("error") val error: String
)