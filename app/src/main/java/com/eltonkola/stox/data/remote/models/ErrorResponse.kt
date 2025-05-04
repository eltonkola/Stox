package com.eltonkola.stox.data.remote.models

import com.google.gson.annotations.SerializedName

/**
 * Model for the error response from Polygon API
 */
data class ErrorResponse(
    @SerializedName("status") val status: String,
    @SerializedName("request_id") val requestId: String,
    @SerializedName("error") val error: String
)