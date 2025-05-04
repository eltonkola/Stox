package com.eltonkola.stox.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stocks")
data class Stock(
    @PrimaryKey val symbol: String,
    val name: String,
    val currentPrice: Double,
    val dailyChange: Double,
    val changePercentage: Double,
    val lastUpdated: Long,
    val creationDate: Long = System.currentTimeMillis(),
)
