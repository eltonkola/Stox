package com.eltonkola.core_domain.usecases

import com.eltonkola.core_data.local.entities.Stock
import com.eltonkola.core_data.repository.StockRepository
import kotlinx.coroutines.flow.Flow

class GetAllStocksUseCase(private val repository: StockRepository) {
    operator fun invoke(): Flow<List<Stock>> = repository.allStocks
}
