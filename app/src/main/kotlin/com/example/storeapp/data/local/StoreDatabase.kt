package com.example.storeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.storeapp.data.local.dao.ProductCartDao
import com.example.storeapp.data.local.entity.ProductCartEntity

@Database(entities = [ProductCartEntity::class], version = 1, exportSchema = false)
abstract class StoreDatabase : RoomDatabase() {

    abstract fun productCartDao(): ProductCartDao

    companion object {

        const val DATABASE_NAME = "store_database"
    }
}
