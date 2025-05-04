package com.eltonkola.feature_stock_detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.eltonkola.core_data.local.entities.StockDetails
import com.eltonkola.core_ui.components.ErrorDialog

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StockDetailScreen(
    stockSymbol: String,
    viewModel: StockDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val stockDetails by viewModel.stockDetails.collectAsState()
    val historicalData by viewModel.historicalData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val isErrorDialogVisible by viewModel.isErrorDialogVisible.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stockSymbol) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 2.dp,
                    bottom = 2.dp
                )
            ) {
                // Price Chart
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.elevatedCardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            if (historicalData.isNotEmpty()) {
                                StockPriceChart(historicalData)
                            } else {
                                Text(
                                    modifier = Modifier.align(Alignment.Center),
                                    text = "No historical data available",
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                if (stockDetails != null) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            elevation = CardDefaults.elevatedCardElevation(4.dp)
                        ) {
                            StockHeader(
                                modifier = Modifier.fillMaxWidth(),
                                logoUrl = stockDetails!!.branding,
                                name = stockDetails!!.symbol,
                                ticker = stockDetails!!.description
                            )
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.elevatedCardElevation(4.dp)
                    ) {
                        if (stockDetails != null) {
                            MinimalData(stockDetails!!)
                        } else {
                            Text(
                                text = "No details available",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                if (stockDetails != null && !stockDetails!!.incompleteProfile) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            elevation = CardDefaults.elevatedCardElevation(4.dp)
                        ) {
                            StockData(stockDetails!!)
                        }
                    }
                }
            }
        }
    }


    if (isErrorDialogVisible) {
        ErrorDialog(
            error = error,
            onDismiss = { viewModel.dismissRateLimitDialog() },
            onRetry = { viewModel.retry() }
        )
    }

}


@Composable
fun MinimalData(stockDetails: StockDetails) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Details",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        DetailRow("Open", "$${stockDetails.openPrice}")
        DetailRow("High", "$${stockDetails.highPrice}")
        DetailRow("Low", "$${stockDetails.lowPrice}")
        DetailRow("Volume", "${stockDetails.volume}")

        if (stockDetails.marketCap != null) {
            DetailRow("Market Cap", formatMarketCap(stockDetails.marketCap!!))
        }


    }
}

@Composable
fun StockData(stockDetails: StockDetails) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Other information",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (stockDetails.phoneNumber != null) {
            DetailRow("Phone Number", stockDetails.phoneNumber!!)
        }
        if (stockDetails.address != null) {
            DetailRow("Address", stockDetails.address!!)
        }
        if (stockDetails.sicDescription != null) {
            DetailRow("SIC Description", stockDetails.sicDescription!!)
        }
        if (stockDetails.homepageUrl != null) {
            HomepageRow("Homepage", stockDetails.homepageUrl!!)
        }
        if (stockDetails.totalEmployees != null) {
            DetailRow("Total Employees", stockDetails.totalEmployees.toString())
        }
        if (stockDetails.listDate != null) {
            DetailRow("List Date", stockDetails.listDate!!)
        }

    }
}

@Composable
fun StockHeader(
    modifier: Modifier = Modifier,
    logoUrl: String?,
    name: String?,
    ticker: String?
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(logoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "$name Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Name and Ticker
            Column(modifier = Modifier.weight(1.0f)) { // Take remaining space
                if (!name.isNullOrBlank()) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (!ticker.isNullOrBlank()) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = ticker,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

    }

}


private fun formatMarketCap(marketCap: Double): String {
    return when {
        marketCap >= 1_000_000_000_000 -> String.format(
            "%.2fT",
            marketCap / 1_000_000_000_000.0
        ) // Trillions
        marketCap >= 1_000_000_000 -> String.format(
            "%.2fB",
            marketCap / 1_000_000_000.0
        ) // Billions
        marketCap >= 1_000_000 -> String.format("%.1fM", marketCap / 1_000_000.0) // Millions
        marketCap >= 1_000 -> String.format("%dK", marketCap / 1_000) // Thousands
        else -> "$marketCap"
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun HomepageRow(label: String, url: String) {
    val uriHandler = LocalUriHandler.current
    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(url) // You could shorten this display text if needed
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        ClickableText(
            text = annotatedString,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(start = 8.dp),
            onClick = {
                try {
                    uriHandler.openUri(url)
                } catch (e: Exception) {
                    // Handle exceptions like invalid URL format
                    println("Could not open URL: $e")
                }
            }
        )
    }
}
