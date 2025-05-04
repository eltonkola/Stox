package com.eltonkola.stox.ui.screens.addstock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltonkola.stox.data.remote.models.TickerItem
import com.eltonkola.stox.data.repository.RepositoryError
import com.eltonkola.stox.domain.usecases.AddStockUseCase
import com.eltonkola.stox.domain.usecases.GetErrorUseCase
import com.eltonkola.stox.domain.usecases.ResetErrorUseCase
import com.eltonkola.stox.domain.usecases.SearchStocksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class AddStockViewModel @Inject constructor(
    private val searchStocksUseCase: SearchStocksUseCase,
    private val addStockUseCase: AddStockUseCase,
    getErrorUseCase: GetErrorUseCase,
    private val resetErrorUseCase: ResetErrorUseCase,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchResults = MutableStateFlow<List<TickerItem>>(emptyList())
    val searchResults: StateFlow<List<TickerItem>> = _searchResults

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    private val _isErrorDialogVisible = MutableStateFlow(false)
    val isErrorDialogVisible = _isErrorDialogVisible.asStateFlow()
    val error = getErrorUseCase()

    // Debounce search queries
    init {
        observeErrors()
        viewModelScope.launch {
            searchQuery
                .debounce(300)
                .filter { it.length >= 2 }
                .distinctUntilChanged()
                .collect { query ->
                    searchStocks(query)
                }
        }
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

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.length < 2) {
            _searchResults.value = emptyList()
        }
    }

    private fun searchStocks(query: String) {
        viewModelScope.launch {
            _isSearching.value = true
            try {
                _searchResults.value = searchStocksUseCase(query)
            } finally {
                _isSearching.value = false
            }
        }
    }

    fun addStock(symbol: String, name: String) {
        viewModelScope.launch {
            addStockUseCase(symbol, name)
        }
    }

    fun retry(){
        resetErrorUseCase()
        onSearchQueryChange(_searchQuery.value)
    }

}
