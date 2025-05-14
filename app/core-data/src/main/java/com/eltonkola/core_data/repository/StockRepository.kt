package com.eltonkola.core_data.repository

import android.util.Log
import com.eltonkola.core_data.local.dao.StockDao
import com.eltonkola.core_data.local.entities.Stock
import com.eltonkola.core_data.local.entities.StockDetails
import com.eltonkola.core_data.local.model.RateLimitException
import com.eltonkola.core_data.local.model.RepositoryError
import com.eltonkola.core_data.local.model.isRateLimitError
import com.eltonkola.core_data.remote.api.PolygonService
import com.eltonkola.core_data.remote.models.HistoricalDataPoint
import com.eltonkola.core_data.remote.models.TickerItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException

class StockRepository(
    private val stockDao: StockDao,
    private val polygonService: PolygonService,
    private val apiKey: String
) {

    private val _error = MutableStateFlow<RepositoryError>(RepositoryError.None)
    val error: StateFlow<RepositoryError> = _error

    fun resetError() {
        _error.value = RepositoryError.None
    }


    // Get all tracked stocks
    val allStocks: Flow<List<Stock>> = stockDao.getAllStocksFlow()

    // Get stock details by symbol
    suspend fun getStockDetails(symbol: String): StockDetails? {
        val localDetails = stockDao.getStockDetails(symbol)
        return if (localDetails != null) {
            localDetails
        } else {
            refreshStockDetails(symbol)
            stockDao.getStockDetails(symbol)
        }
    }

    suspend fun getStockLocalData(symbol: String): StockDetails? {
        val localDetails = stockDao.getStockDetails(symbol)
        return if (localDetails != null) {
            localDetails
        } else {
            refreshStockDetails(symbol)
            stockDao.getStockDetails(symbol)
        }
    }

    suspend fun getStockDetailsExtra(symbol: String): StockDetails? {
        val localDetails = stockDao.getStockDetails(symbol)
        return if (localDetails != null && !localDetails.incompleteProfile) {
            localDetails
        } else {
            refreshStockExtraDetails(symbol)
            stockDao.getStockDetails(symbol)
        }
    }


    // Refresh stock data
    suspend fun refreshStockData(): Boolean {
        try {
            val stocks = stockDao.getAllStocksFlow().first()
            stocks.forEach { stock ->
                if (!refreshStockData(stock.symbol)) {
                    return false // Stop if any refresh fails
                }
            }
            return true
        } catch (e: Exception) {
            handleException(e)
            return false
        }
    }

    suspend fun getStock(symbol: String): Stock? {
        return stockDao.getStockBySymbol(symbol)
    }
    suspend fun updateStock(stock: Stock) {
        return stockDao.updateStock(stock)
    }


    // Refresh specific stock data
    suspend fun refreshStockData(symbol: String): Boolean {
        return try {
            val stockData = stockDao.getStockBySymbol(symbol)
            if (stockData == null) {
                return false
            }
            val response = polygonService.getTickerDetails(symbol, apiKey)
            if (response.resultsCount > 0) {
                val result = response.results[0]

                stockDao.updateStock(
                    stockData.copy(
                        currentPrice = result.c,
                        dailyChange = result.c - result.o,
                        changePercentage = ((result.c - result.o) / result.o) * 100,
                        lastUpdated = System.currentTimeMillis()
                    )
                )
            }
            true
        } catch (e: Exception) {
            handleException(e)
            false
        }
    }

    // Refresh stock details
    suspend fun refreshStockDetails(symbol: String): Boolean {
        return try {
            val stockDetails = stockDao.getStockDetails(symbol)
            val response = polygonService.getTickerDetails(symbol, apiKey)
            if (response.resultsCount > 0) {
                val result = response.results[0]
                if (stockDetails == null) {
                    val newStockDetails = StockDetails(
                        symbol = symbol,
                        volume = result.v,
                        openPrice = result.o,
                        highPrice = result.h,
                        lowPrice = result.l,
                    )
                    stockDao.insertStockDetails(newStockDetails)
                } else {
                    stockDao.updateStockDetails(
                        stockDetails.copy(
                            volume = result.v,
                            openPrice = result.o,
                            highPrice = result.h,
                            lowPrice = result.l,
                        )
                    )
                }
            }
            true
        } catch (e: Exception) {
            handleException(e)
            false
        }
    }

    suspend fun refreshStockExtraDetails(symbol: String): Boolean {
        return try {
            val stockDetails = stockDao.getStockDetails(symbol)
            if (stockDetails == null) {
                return false
            }
            val response = polygonService.getStockDetails(symbol, apiKey)
            val result = response.results
            stockDao.updateStockDetails(
                stockDetails.copy(
                    marketCap = result.marketCap,
                    phoneNumber = result.phoneNumber,
                    address = result.address?.address,
                    description = result.description,
                    sicDescription = result.sicDescription,
                    homepageUrl = result.homepageUrl,
                    totalEmployees = result.totalEmployees,
                    listDate = result.listDate,
                    branding = (result.branding?.iconUrl ?: result.branding?.logoUrl)?.plus("?apiKey=$apiKey"),
                )
            )
            true
        } catch (e: Exception) {
            handleException(e)
            false
        }
    }

    // Search stocks
    suspend fun searchStocks(query: String): List<TickerItem> {
        return try {
            val response = polygonService.searchTickers(query, apiKey = apiKey)
            response.results
        } catch (e: Exception) {
            handleException(e)
            emptyList()
        }
    }

    // Add stock to tracked list
    suspend fun addStock(symbol: String, name: String): Boolean {
        return try {
            val response = polygonService.getTickerDetails(symbol, apiKey)
            if (response.resultsCount > 0) {
                val result = response.results[0]
                val stock = Stock(
                    symbol = symbol,
                    name = name,
                    currentPrice = result.c,
                    dailyChange = result.c - result.o,
                    changePercentage = ((result.c - result.o) / result.o) * 100,
                    lastUpdated = System.currentTimeMillis()
                )
                stockDao.insertStock(stock)

                // Also fetch and store details
                refreshStockDetails(symbol)
            }
            true
        } catch (e: Exception) {
            handleException(e)
            false
        }
    }

    // Remove stock from tracked list
    suspend fun removeStock(symbol: String): Boolean {
        return try {
            stockDao.getStockBySymbol(symbol)?.let {
                stockDao.deleteStock(it)
            }
            true
        } catch (e: Exception) {
            _error.value = RepositoryError.GeneralError("Failed to remove stock: ${e.message}")
            false
        }
    }

    // Get historical data for a stock
    suspend fun getHistoricalData(
        symbol: String,
        multiplier: Int = 1,
        timespan: String = "day",
        from: String,
        to: String
    ): List<HistoricalDataPoint> {
        return try {
            val response = polygonService.getHistoricalData(
                ticker = symbol,
                multiplier = multiplier,
                timespan = timespan,
                from = from,
                to = to,
                apiKey = apiKey
            )
            response.results
        } catch (e: Exception) {
            handleException(e)
            emptyList()
        }
    }

    // Helper function to handle exceptions
    private fun handleException(e: Exception) {
        Log.e("StockRepository", "API Error: ${e.localizedMessage}")

        _error.value = when {
            e is RateLimitException -> {
                RepositoryError.RateLimit(
                    e.message
                        ?: "You've exceeded the maximum requests per minute. Please wait or upgrade your subscription."
                )
            }

            e.isRateLimitError() -> {
                RepositoryError.RateLimit("You've exceeded the maximum requests per minute. Please wait or upgrade your subscription.")
            }

            e is IOException -> {
                RepositoryError.NetworkError("Network error: ${e.message}")
            }

            e is HttpException -> {
                if (e.code() == 429) {
                    RepositoryError.RateLimit("You've exceeded the maximum requests per minute. Please wait or upgrade your subscription.")
                } else {
                    RepositoryError.NetworkError("HTTP error ${e.code()}: ${e.message()}")
                }
            }

            else -> {
                RepositoryError.GeneralError("Error: ${e.message}")
            }
        }
    }
}