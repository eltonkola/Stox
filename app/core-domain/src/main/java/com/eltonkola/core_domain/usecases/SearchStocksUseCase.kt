package com.eltonkola.core_domain.usecases

import com.eltonkola.core_data.remote.models.TickerItem
import com.eltonkola.core_data.repository.StockRepository

class SearchStocksUseCase(private val repository: StockRepository) {
    suspend operator fun invoke(query: String): List<TickerItem> {
        return repository.searchStocks(query)
    }
}
