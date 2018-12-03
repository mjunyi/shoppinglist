package com.example.jmin.shoppinglistapp.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "shoppingitem")
data class ShoppingItem(
        @PrimaryKey(autoGenerate = true) var itemId: Long?,
        @ColumnInfo(name = "type") var itemType: String,
        @ColumnInfo(name = "name") var itemName: String,
        @ColumnInfo(name = "quantity") var itemQuantity: Int,
        @ColumnInfo(name = "description") var itemDescription: String,
        @ColumnInfo(name = "purchased") var purchased: Boolean,
        @ColumnInfo(name = "price") var itemPrice: Int
) : Serializable