package com.eltonkola.core_domain.usecases

import com.eltonkola.core_data.repository.StockRepository

class AddStockUseCase(private val repository: StockRepository) {
    suspend operator fun invoke(symbol: String, name: String) {
        repository.addStock(symbol, name)
    }
}