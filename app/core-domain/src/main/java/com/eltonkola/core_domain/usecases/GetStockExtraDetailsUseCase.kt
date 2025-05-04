package com.eltonkola.core_domain.usecases

import com.eltonkola.core_data.local.entities.StockDetails
import com.eltonkola.core_data.repository.StockRepository


class GetStockExtraDetailsUseCase(private val repository: StockRepository) {
    suspend operator fun invoke(symbol: String): StockDetails? {
        return repository.getStockDetailsExtra(symbol)
    }
}