package com.eltonkola.core_domain.usecases

import com.eltonkola.core_data.local.model.RepositoryError
import com.eltonkola.core_data.repository.StockRepository
import kotlinx.coroutines.flow.StateFlow

class GetErrorUseCase(private val repository: StockRepository) {
    operator fun invoke(): StateFlow<RepositoryError>  {
        return repository.error
    }
}