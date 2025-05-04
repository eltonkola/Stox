package com.eltonkola.stox.domain.usecases

import com.eltonkola.stox.data.repository.StockRepository

class AddStockUseCase(private val repository: StockRepository) {
    suspend operator fun invoke(symbol: String, name: String) {
        repository.addStock(symbol, name)
    }
}