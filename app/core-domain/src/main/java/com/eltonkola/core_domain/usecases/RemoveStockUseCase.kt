package com.eltonkola.core_domain.usecases

import com.eltonkola.core_data.repository.StockRepository

class RemoveStockUseCase(private val repository: StockRepository) {
    suspend operator fun invoke(symbol: String) {
        repository.removeStock(symbol)
    }
}