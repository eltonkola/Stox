package com.eltonkola.feature_edit_stock

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditStockScreen(
    viewModel: EditStockViewModel = hiltViewModel(),
    stockID: String,
    onBackClick: () -> Unit,
) {
    val stockDetails by viewModel.stockDetails.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadStock(stockID)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Stock - $stockID") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {


            if (stockDetails != null) {

                var text = remember { mutableStateOf(stockDetails!!.nrStocks.toString()) }

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                    ,
                    value = text.value,
                    onValueChange = {
                        text.value = it
                        if(it.isNotEmpty()){
                            viewModel.setStockNrStocks(it.toInt())
                        }
                    },
                    placeholder = { Text("How much of this stock do you have?") },
                    leadingIcon = {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Nr of stocks")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.None
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onBackClick()
                        }
                    )
                )

            }else{
                CircularProgressIndicator()
            }


        }
    }
}
