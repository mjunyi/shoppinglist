package com.example.jmin.shoppinglistapp.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.jmin.shoppinglistapp.R

@Database(entities = arrayOf(ShoppingItem::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun shoppingDao(): ShoppingItemDAO

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, context.getString(R.string.shoppinglist_db))
                        .build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}