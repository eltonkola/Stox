package com.eltonkola.stox.domain.usecases

import com.eltonkola.stox.data.local.entities.Stock
import com.eltonkola.stox.data.repository.StockRepository
import kotlinx.coroutines.flow.Flow

class GetAllStocksUseCase(private val repository: StockRepository) {
    operator fun invoke(): Flow<List<Stock>> = repository.allStocks
}
