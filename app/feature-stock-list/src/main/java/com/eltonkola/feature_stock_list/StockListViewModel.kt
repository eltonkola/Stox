package com.eltonkola.feature_stock_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltonkola.core_data.local.entities.Stock
import com.eltonkola.core_domain.usecases.GetAllStocksUseCase
import com.eltonkola.core_domain.usecases.RemoveStockUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockListViewModel @Inject constructor(
    getAllStocksUseCase: GetAllStocksUseCase,
    private val removeStockUseCase: RemoveStockUseCase
) : ViewModel() {

    val stocks: StateFlow<List<Stock>> = getAllStocksUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun removeStock(symbol: String) {
        viewModelScope.launch {
            removeStockUseCase(symbol)
        }
    }
}
