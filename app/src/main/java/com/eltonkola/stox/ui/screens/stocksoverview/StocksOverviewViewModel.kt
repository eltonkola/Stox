package com.eltonkola.stox.ui.screens.stocksoverview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltonkola.stox.data.local.entities.Stock
import com.eltonkola.stox.domain.usecases.GetAllStocksUseCase
import com.eltonkola.stox.domain.usecases.GetErrorUseCase
import com.eltonkola.stox.domain.usecases.RefreshStockDataUseCase
import com.eltonkola.stox.domain.usecases.ResetErrorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StocksOverviewViewModel @Inject constructor(
    getAllStocksUseCase: GetAllStocksUseCase,
    private val refreshStockDataUseCase: RefreshStockDataUseCase,
    getErrorUseCase: GetErrorUseCase,
    private val resetErrorUseCase: ResetErrorUseCase,
) : ViewModel() {

    val stocks: StateFlow<List<Stock>> = getAllStocksUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    val error = getErrorUseCase()

    init {
        refreshStocks()
    }

    fun dismissRateLimitDialog() {
        resetErrorUseCase()
    }

    fun retryAfterRateLimit() {
        resetErrorUseCase()
        refreshStocks()
    }

    fun refreshStocks() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                refreshStockDataUseCase()
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}
