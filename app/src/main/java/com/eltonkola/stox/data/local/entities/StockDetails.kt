package com.eltonkola.stox.data.local.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "stock_details")
data class StockDetails(
    @PrimaryKey val symbol: String,
    val volume: Long,
    val openPrice: Double,
    val highPrice: Double,
    val lowPrice: Double,

    val marketCap: Double? = null,
    val phoneNumber: String? = null,
    val address: String? = null,
    val description: String? = null,
    val sicDescription: String? = null,
    val homepageUrl: String? = null,
    val totalEmployees: Int? = null,
    val listDate: String? = null,
    val branding: String? = null
){
    @Ignore
    val incompleteProfile: Boolean = description == null
}
