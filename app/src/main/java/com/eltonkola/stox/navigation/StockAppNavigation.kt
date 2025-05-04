package com.eltonkola.stox.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.eltonkola.feature_add_stock.AddStockScreen
import com.eltonkola.feature_stock_detail.StockDetailScreen
import com.eltonkola.feature_stock_list.StockListScreen
import com.eltonkola.feature_stocks_overview.StocksOverviewScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StockAppNavigation(
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = "stocks_overview"
    ) {
        composable("stocks_overview") {
            StocksOverviewScreen(
                onStockClick = { stockSymbol ->
                    navController.navigate("stock_detail/$stockSymbol")
                },
                onManageStocksClick = {
                    navController.navigate("stock_list")
                }
            )
        }

        composable(
            "stock_detail/{stockSymbol}",
            arguments = listOf(navArgument("stockSymbol") { type = NavType.StringType })
        ) { backStackEntry ->
            val stockSymbol = backStackEntry.arguments?.getString("stockSymbol") ?: ""
            StockDetailScreen(
                stockSymbol = stockSymbol,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("stock_list") {
            StockListScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onAddStockClick = {
                    navController.navigate("add_stock")
                }
            )
        }

        composable("add_stock") {
            AddStockScreen(
                onDismiss = {
                    navController.popBackStack()
                },
                onStockAdded = {
                    navController.popBackStack()
                }
            )
        }
    }

}