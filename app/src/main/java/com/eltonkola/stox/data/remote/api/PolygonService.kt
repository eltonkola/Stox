package com.eltonkola.stox.data.remote.api

import com.eltonkola.stox.data.remote.models.HistoricalDataResponse
import com.eltonkola.stox.data.remote.models.SearchTickersResponse
import com.eltonkola.stox.data.remote.models.StockDetailsResponse
import com.eltonkola.stox.data.remote.models.TickerDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PolygonService {
    @GET("v2/aggs/ticker/{ticker}/prev")
    suspend fun getTickerDetails(
        @Path("ticker") ticker: String,
        @Query("apiKey") apiKey: String
    ): TickerDetailsResponse

    @GET("v3/reference/tickers")
    suspend fun searchTickers(
        @Query("search") query: String,
        @Query("active") active: Boolean = true,
        @Query("sort") sort: String = "ticker",
        @Query("order") order: String = "asc",
        @Query("limit") limit: Int = 10,
        @Query("apiKey") apiKey: String
    ): SearchTickersResponse

    // Add endpoints for historical data
    @GET("v2/aggs/ticker/{ticker}/range/{multiplier}/{timespan}/{from}/{to}")
    suspend fun getHistoricalData(
        @Path("ticker") ticker: String,
        @Path("multiplier") multiplier: Int,
        @Path("timespan") timespan: String,
        @Path("from") from: String,
        @Path("to") to: String,
        @Query("apiKey") apiKey: String
    ): HistoricalDataResponse


    //get ticker details
    @GET("v3/reference/tickers/{ticker}")
    suspend fun getStockDetails(
        @Path("ticker") ticker: String,
        @Query("apiKey") apiKey: String
    ): StockDetailsResponse




}
