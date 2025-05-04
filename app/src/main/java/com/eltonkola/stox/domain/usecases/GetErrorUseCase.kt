package com.eltonkola.stox.domain.usecases

import com.eltonkola.stox.data.repository.RepositoryError
import com.eltonkola.stox.data.repository.StockRepository
import kotlinx.coroutines.flow.StateFlow

class GetErrorUseCase(private val repository: StockRepository) {
    operator fun invoke(): StateFlow<RepositoryError>  {
        return repository.error
    }
}