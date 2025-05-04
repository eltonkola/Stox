package com.eltonkola.stox.ui.screens.stockdetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltonkola.stox.data.local.entities.StockDetails
import com.eltonkola.stox.data.remote.models.HistoricalDataPoint
import com.eltonkola.stox.data.repository.RepositoryError
import com.eltonkola.stox.domain.usecases.GetErrorUseCase
import com.eltonkola.stox.domain.usecases.GetHistoricalDataUseCase
import com.eltonkola.stox.domain.usecases.GetStockDetailsUseCase
import com.eltonkola.stox.domain.usecases.GetStockExtraDetailsUseCase
import com.eltonkola.stox.domain.usecases.ResetErrorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class StockDetailViewModel @Inject constructor(
    private val getStockDetailsUseCase: GetStockDetailsUseCase,
    private val getHistoricalDataUseCase: GetHistoricalDataUseCase,
    private val getStockExtraDetailsUseCase: GetStockExtraDetailsUseCase,
    getErrorUseCase: GetErrorUseCase,
    private val resetErrorUseCase: ResetErrorUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val stockSymbol: String = checkNotNull(savedStateHandle["stockSymbol"])

    private val _stockDetails = MutableStateFlow<StockDetails?>(null)
    val stockDetails: StateFlow<StockDetails?> = _stockDetails

    private val _historicalData = MutableStateFlow<List<HistoricalDataPoint>>(emptyList())
    val historicalData: StateFlow<List<HistoricalDataPoint>> = _historicalData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isErrorDialogVisible = MutableStateFlow(false)
    val isErrorDialogVisible = _isErrorDialogVisible.asStateFlow()
    val error = getErrorUseCase()

    init {
        observeErrors()
        loadStockDetails()
        loadHistoricalData()
    }

    private fun observeErrors() {
        viewModelScope.launch {
            error.collect { error ->
                _isErrorDialogVisible.value = error !is RepositoryError.None
            }
        }
    }

    fun dismissRateLimitDialog() {
        resetErrorUseCase()
    }

    fun retry() {
        resetErrorUseCase()
        loadStockDetails()
        loadHistoricalData()
    }

    private fun loadStockDetails() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _stockDetails.value = getStockDetailsUseCase(stockSymbol)
                // Load extra details
                _stockDetails.value =getStockExtraDetailsUseCase(stockSymbol)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadHistoricalData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _historicalData.value = getHistoricalDataUseCase(stockSymbol)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
