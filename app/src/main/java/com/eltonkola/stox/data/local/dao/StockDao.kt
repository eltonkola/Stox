package com.eltonkola.stox.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.eltonkola.stox.data.local.entities.Stock
import com.eltonkola.stox.data.local.entities.StockDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Query("SELECT * FROM stocks order by creationDate desc")
    fun getAllStocksFlow(): Flow<List<Stock>>

    @Query("SELECT * FROM stocks WHERE symbol = :symbol")
    suspend fun getStockBySymbol(symbol: String): Stock?

    @Query("SELECT * FROM stock_details WHERE symbol = :symbol")
    suspend fun getStockDetails(symbol: String): StockDetails?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stock: Stock)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStockDetails(stockDetails: StockDetails)

    @Update
    suspend fun updateStockDetails(stockDetails: StockDetails)

    @Delete
    suspend fun deleteStock(stock: Stock)

    @Update
    suspend fun updateStock(stock: Stock)
}
