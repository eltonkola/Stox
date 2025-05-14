package com.eltonkola.feature_edit_stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltonkola.core_data.local.entities.Stock
import com.eltonkola.core_domain.usecases.GetLocalStockUseCase
import com.eltonkola.core_domain.usecases.UpdateLocalStockUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class EditStockViewModel @Inject constructor(
    private val getLocalStockUseCase: GetLocalStockUseCase,
    private val updateLocalStockUseCase: UpdateLocalStockUseCase,
) : ViewModel() {

    private val _stockDetails = MutableStateFlow<Stock?>(null)
    val stockDetails: StateFlow<Stock?> = _stockDetails

    fun loadStock(stockSymbol: String) {
        viewModelScope.launch {
            _stockDetails.value = getLocalStockUseCase(stockSymbol)
        }
    }

    fun setStockNrStocks(nrStocks: Int){
        viewModelScope.launch {
            val stock = _stockDetails.value?.copy(nrStocks = nrStocks)
            stock?.let{
                updateLocalStockUseCase(it)
            }
        }
    }

}
