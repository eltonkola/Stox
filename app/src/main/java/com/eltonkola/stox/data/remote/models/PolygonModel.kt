package com.eltonkola.stox.data.remote.models

// Response data classes
data class TickerDetailsResponse(
    val ticker: String,
    val queryCount: Int,
    val resultsCount: Int,
    val results: List<TickerResult>
)

data class TickerResult(
    val v: Long, // Volume
    val o: Double, // Open
    val c: Double, // Close
    val h: Double, // High
    val l: Double, // Low
    val t: Long // Timestamp
)

data class SearchTickersResponse(
    val results: List<TickerItem>,
    val status: String,
    val count: Int
)

data class TickerItem(
    val ticker: String,
    val name: String,
    val market: String,
    val locale: String,
    val type: String,
    val active: Boolean,
    val primaryExch: String
)

data class HistoricalDataResponse(
    val ticker: String,
    val queryCount: Int,
    val resultsCount: Int,
    val adjusted: Boolean,
    val results: List<HistoricalDataPoint>
)

data class HistoricalDataPoint(
    val v: Long, // Volume
    val o: Double, // Open
    val c: Double, // Close
    val h: Double, // High
    val l: Double, // Low
    val t: Long // Timestamp
)
