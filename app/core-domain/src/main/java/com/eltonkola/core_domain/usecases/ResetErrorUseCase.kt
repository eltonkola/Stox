package com.eltonkola.core_domain.usecases

import com.eltonkola.core_data.repository.StockRepository

class ResetErrorUseCase(private val repository: StockRepository) {
    operator fun invoke() {
        repository.resetError()
    }
}