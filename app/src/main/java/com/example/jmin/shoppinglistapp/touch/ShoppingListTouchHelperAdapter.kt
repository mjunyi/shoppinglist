package com.example.jmin.shoppinglistapp.touch

interface ShoppingListTouchHelperAdapter{
    fun onDismiss(position: Int)
    fun onItemMoved(fromPosition: Int, toPosition: Int)
}