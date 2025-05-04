package com.eltonkola.stox.ui.screens.stocksoverview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eltonkola.stox.data.repository.RepositoryError
import com.eltonkola.stox.domain.timeAgo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StocksOverviewScreen(
    viewModel: StocksOverviewViewModel = hiltViewModel(),
    onStockClick: (String) -> Unit,
    onManageStocksClick: () -> Unit
) {
    val stocks by viewModel.stocks.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val error by viewModel.error.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(error) {
       val errorMessage = when (error) {
           is RepositoryError.RateLimit -> {
               (error as RepositoryError.RateLimit).message
           }
           is RepositoryError.GeneralError -> {
               (error as RepositoryError.GeneralError).message
           }
           is RepositoryError.NetworkError -> {
               (error as RepositoryError.NetworkError).message
           }
           is RepositoryError.None -> {
               null
           }
       }

        if(errorMessage!=null){
            val result = snackbarHostState.showSnackbar(
                message = errorMessage,
                actionLabel = "Retry",
                duration = SnackbarDuration.Long,
                withDismissAction = true
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.retryAfterRateLimit()
            }else if (result == SnackbarResult.Dismissed) {
                viewModel.dismissRateLimitDialog()
            }
        }

    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Stocks Overview",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        if(stocks.isNotEmpty()) {
                            Text(
                                text = "Last updated: ${stocks.last().lastUpdated.timeAgo()}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onManageStocksClick) {
                        Icon(Icons.AutoMirrored.Default.List, contentDescription = "Manage Stocks")
                    }
                }
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refreshStocks() }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(stocks) { stock ->
                    StockItem(
                        stock = stock,
                        onClick = { onStockClick(stock.symbol) }
                    )
                }

                if (stocks.isEmpty() && !isRefreshing) {
                    item {
                        EmptyStocksList(onManageStocksClick = onManageStocksClick)
                    }
                }
            }
        }


    }
}
