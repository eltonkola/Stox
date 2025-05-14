package com.eltonkola.core_domain.usecases

import com.eltonkola.core_data.local.entities.Stock
import com.eltonkola.core_data.repository.StockRepository


class GetLocalStockUseCase(private val repository: StockRepository) {
    suspend operator fun invoke(symbol: String): Stock? {
        return repository.getStock(symbol)
    }
}