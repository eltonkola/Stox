package com.eltonkola.stox.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eltonkola.stox.data.local.dao.StockDao
import com.eltonkola.stox.data.local.entities.Stock
import com.eltonkola.stox.data.local.entities.StockDetails

@Database(entities = [Stock::class, StockDetails::class], version = 2)
abstract class StockDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao

    companion object {
        @Volatile
        private var INSTANCE: StockDatabase? = null

        fun getDatabase(context: Context): StockDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    StockDatabase::class.java,
                    "stock_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
