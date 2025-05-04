package com.eltonkola.core_domain.usecases

import com.eltonkola.core_data.repository.StockRepository


class RefreshStockDataUseCase(private val repository: StockRepository) {
    suspend operator fun invoke() {
        repository.refreshStockData()
    }
}
