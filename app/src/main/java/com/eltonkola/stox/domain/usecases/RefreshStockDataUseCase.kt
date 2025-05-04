package com.eltonkola.stox.domain.usecases

import com.eltonkola.stox.data.repository.StockRepository

class RefreshStockDataUseCase(private val repository: StockRepository) {
    suspend operator fun invoke() {
        repository.refreshStockData()
    }
}
