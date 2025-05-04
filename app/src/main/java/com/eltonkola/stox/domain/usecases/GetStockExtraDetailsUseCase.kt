package com.eltonkola.stox.domain.usecases

import com.eltonkola.stox.data.local.entities.StockDetails
import com.eltonkola.stox.data.repository.StockRepository

class GetStockExtraDetailsUseCase(private val repository: StockRepository) {
    suspend operator fun invoke(symbol: String): StockDetails? {
        return repository.getStockDetailsExtra(symbol)
    }
}