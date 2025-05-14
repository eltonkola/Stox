package com.eltonkola.core_domain.usecases

import com.eltonkola.core_data.local.entities.Stock
import com.eltonkola.core_data.repository.StockRepository


class UpdateLocalStockUseCase(private val repository: StockRepository) {
    suspend operator fun invoke(stock: Stock) {
        return repository.updateStock(stock)
    }
}