package com.eltonkola.stox.domain.usecases

import com.eltonkola.stox.data.remote.models.TickerItem
import com.eltonkola.stox.data.repository.StockRepository

class SearchStocksUseCase(private val repository: StockRepository) {
    suspend operator fun invoke(query: String): List<TickerItem> {
        return repository.searchStocks(query)
    }
}
