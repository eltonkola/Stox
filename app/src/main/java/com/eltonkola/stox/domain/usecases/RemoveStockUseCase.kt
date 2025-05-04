package com.eltonkola.stox.domain.usecases

import com.eltonkola.stox.data.repository.StockRepository

class RemoveStockUseCase(private val repository: StockRepository) {
    suspend operator fun invoke(symbol: String) {
        repository.removeStock(symbol)
    }
}