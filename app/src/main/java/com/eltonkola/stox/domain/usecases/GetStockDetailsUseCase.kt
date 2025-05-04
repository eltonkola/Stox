package com.eltonkola.stox.domain.usecases

import com.eltonkola.stox.data.local.entities.StockDetails
import com.eltonkola.stox.data.repository.StockRepository

class GetStockDetailsUseCase(private val repository: StockRepository) {
    suspend operator fun invoke(symbol: String): StockDetails? {
        return repository.getStockDetails(symbol)
    }
}