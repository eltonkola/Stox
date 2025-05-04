package com.eltonkola.stox.ui.screens.stockdetail

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eltonkola.stox.data.remote.models.HistoricalDataPoint
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberCandlestickCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.core.cartesian.axis.Axis
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.candlestickSeries
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StockPriceChart(data: List<HistoricalDataPoint>, modifier: Modifier = Modifier) {
    if (data.isEmpty()) {
        Box(
            modifier = modifier.fillMaxWidth().height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No data available",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        return
    }

    // Sort data by timestamp
    val sortedData = remember(data) {
        data.sortedBy { it.t }
    }

    // Set up chart model producer
    val modelProducer = remember { CartesianChartModelProducer() }

    // Format timestamps for x-axis
    val dateFormatter = remember {
        SimpleDateFormat("MM/dd", Locale.getDefault())
    }

    // Update the chart data when sortedData changes
    LaunchedEffect(sortedData) {
        Log.d("StockPriceChart", "Processing ${sortedData.size} data points")

        // Extract data points from our HistoricalDataPoint objects
        val xValues = sortedData.mapIndexed { index, _ -> index.toFloat() }
        val openValues = sortedData.map { it.o.toFloat() }
        val closeValues = sortedData.map { it.c.toFloat() }
        val lowValues = sortedData.map { it.l.toFloat() }
        val highValues = sortedData.map { it.h.toFloat() }

        // Update the chart model
        modelProducer.runTransaction {
            candlestickSeries(
                x = xValues,
                opening = openValues,
                closing = closeValues,
                low = lowValues,
                high = highValues,
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp)
    ) {

        val latestPrice = sortedData.lastOrNull()?.c ?: 0.0
        val previousPrice = if (sortedData.size > 1) sortedData[sortedData.size - 2].c else latestPrice
        val priceChange = latestPrice - previousPrice
        val priceChangePercent = if (previousPrice != 0.0) (priceChange / previousPrice) * 100 else 0.0

        Text(
            text = "Price: $${String.format("%.2f", latestPrice)}",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        val priceChangeColor = if (priceChange >= 0) Color.Green else Color.Red
        Text(
            text = "$${String.format("%.2f", priceChange)} (${String.format("%.2f", priceChangePercent)}%)",
            style = MaterialTheme.typography.bodyMedium,
            color = priceChangeColor
        )

        CartesianChartHost(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            chart = rememberCartesianChart(
                rememberCandlestickCartesianLayer(
                    rangeProvider = CartesianLayerRangeProvider.fixed(
                        minY = sortedData.minOf { it.l.toFloat() } * 0.995,
                        maxY = sortedData.maxOf { it.h.toFloat() } * 1.005,
                    )
                ),
                startAxis = VerticalAxis.rememberStart(
                    valueFormatter = object : CartesianValueFormatter {
                        override fun format(
                            context: CartesianMeasuringContext,
                            value: Double,
                            verticalAxisPosition: Axis.Position.Vertical?
                        ): CharSequence {
                            return "$"+ "%.2f".format(value)
                        }
                    }
                ),
                bottomAxis = HorizontalAxis.rememberBottom(
                    valueFormatter = object : CartesianValueFormatter {
                        override fun format(
                            context: CartesianMeasuringContext,
                            value: Double,
                            verticalAxisPosition: Axis.Position.Vertical?
                        ): CharSequence {
                            val index = value.toInt()
                            return  dateFormatter.format(Date(sortedData[index].t))
                        }
                    }

                ),
            ),
            modelProducer = modelProducer,
        )
    }
}
