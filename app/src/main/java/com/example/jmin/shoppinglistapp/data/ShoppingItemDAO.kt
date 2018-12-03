package com.example.jmin.shoppinglistapp.data

import android.arch.persistence.room.*

@Dao
interface ShoppingItemDAO {
    @Query("SELECT * FROM shoppingitem")
    fun findAllShoppingItems(): List<ShoppingItem>

    @Query("DELETE FROM shoppingitem")
    fun deleteAll()

    @Insert
    fun insertShoppingItem(item: ShoppingItem): Long

    @Delete
    fun deleteShoppingItem(item: ShoppingItem)

    @Update
    fun updateShoppingItem(item: ShoppingItem)

}