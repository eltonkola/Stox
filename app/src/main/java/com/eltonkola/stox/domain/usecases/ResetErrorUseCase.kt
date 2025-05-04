package com.eltonkola.stox.domain.usecases

import com.eltonkola.stox.data.repository.StockRepository

class ResetErrorUseCase(private val repository: StockRepository) {
    operator fun invoke() {
        repository.resetError()
    }
}