package com.eltonkola.stox.ui.screens.stocklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltonkola.stox.data.local.entities.Stock
import com.eltonkola.stox.domain.usecases.GetAllStocksUseCase
import com.eltonkola.stox.domain.usecases.RemoveStockUseCase
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
