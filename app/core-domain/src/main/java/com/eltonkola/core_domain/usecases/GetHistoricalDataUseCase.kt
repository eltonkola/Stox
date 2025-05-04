package com.eltonkola.core_domain.usecases

import android.os.Build
import androidx.annotation.RequiresApi
import com.eltonkola.core_data.remote.models.HistoricalDataPoint
import com.eltonkola.core_data.repository.StockRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GetHistoricalDataUseCase(private val repository: StockRepository) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(
        symbol: String,
        days: Int = 30
    ): List<HistoricalDataPoint> {
        val toDate = LocalDate.now()
        val fromDate = toDate.minusDays(days.toLong())

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val from = fromDate.format(formatter)
        val to = toDate.format(formatter)

        return repository.getHistoricalData(
            symbol = symbol,
            multiplier = 1,
            timespan = "day",
            from = from,
            to = to
        )
    }
}