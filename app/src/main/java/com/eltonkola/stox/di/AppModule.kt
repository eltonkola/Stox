package com.eltonkola.stox.di

import android.app.Application
import com.eltonkola.stox.BuildConfig
import com.eltonkola.stox.data.local.dao.StockDao
import com.eltonkola.stox.data.local.database.StockDatabase
import com.eltonkola.stox.data.remote.RetrofitClient
import com.eltonkola.stox.data.remote.api.PolygonService
import com.eltonkola.stox.data.repository.StockRepository
import com.eltonkola.stox.domain.usecases.AddStockUseCase
import com.eltonkola.stox.domain.usecases.GetAllStocksUseCase
import com.eltonkola.stox.domain.usecases.GetErrorUseCase
import com.eltonkola.stox.domain.usecases.GetHistoricalDataUseCase
import com.eltonkola.stox.domain.usecases.GetStockDetailsUseCase
import com.eltonkola.stox.domain.usecases.GetStockExtraDetailsUseCase
import com.eltonkola.stox.domain.usecases.RefreshStockDataUseCase
import com.eltonkola.stox.domain.usecases.RemoveStockUseCase
import com.eltonkola.stox.domain.usecases.ResetErrorUseCase
import com.eltonkola.stox.domain.usecases.SearchStocksUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStockDatabase(app: Application): StockDatabase {
        return StockDatabase.getDatabase(app)
    }

    @Provides
    @Singleton
    fun provideStockDao(db: StockDatabase): StockDao {
        return db.stockDao()
    }

    @Provides
    @Singleton
    fun providePolygonService(): PolygonService {
        return RetrofitClient.polygonService
    }

    @Provides
    @Singleton
    fun provideApiKey(): String {
        return BuildConfig.PolygonApiKey
    }

    @Provides
    @Singleton
    fun provideStockRepository(
        stockDao: StockDao,
        polygonService: PolygonService,
        apiKey: String
    ): StockRepository {
        return StockRepository(stockDao, polygonService, apiKey)
    }

    // UseCases
    @Provides
    @Singleton
    fun provideGetAllStocksUseCase(repository: StockRepository): GetAllStocksUseCase {
        return GetAllStocksUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRefreshStockDataUseCase(repository: StockRepository): RefreshStockDataUseCase {
        return RefreshStockDataUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetStockDetailsUseCase(repository: StockRepository): GetStockDetailsUseCase {
        return GetStockDetailsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetStockExtraDetailsUseCase(repository: StockRepository): GetStockExtraDetailsUseCase {
        return GetStockExtraDetailsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetErrorUseCase(repository: StockRepository): GetErrorUseCase {
        return GetErrorUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideResetErrorUseCase(repository: StockRepository): ResetErrorUseCase {
        return ResetErrorUseCase(repository)
    }



    @Provides
    @Singleton
    fun provideSearchStocksUseCase(repository: StockRepository): SearchStocksUseCase {
        return SearchStocksUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddStockUseCase(repository: StockRepository): AddStockUseCase {
        return AddStockUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRemoveStockUseCase(repository: StockRepository): RemoveStockUseCase {
        return RemoveStockUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetHistoricalDataUseCase(repository: StockRepository): GetHistoricalDataUseCase {
        return GetHistoricalDataUseCase(repository)
    }
}
