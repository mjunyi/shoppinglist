package com.example.jmin.shoppinglistapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.jmin.shoppinglistapp.MainActivity
import com.example.jmin.shoppinglistapp.R
import com.example.jmin.shoppinglistapp.ShoppingItemDialog
import com.example.jmin.shoppinglistapp.data.AppDatabase
import com.example.jmin.shoppinglistapp.data.ShoppingItem
import com.example.jmin.shoppinglistapp.touch.ShoppingListTouchHelperAdapter
import kotlinx.android.synthetic.main.shopping_item_row.view.*
import java.util.*

class ShoppingListAdapter: RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>, ShoppingListTouchHelperAdapter {

    var shoppingList = mutableListOf<ShoppingItem>()
    val context: Context

    constructor(context: Context, items: List<ShoppingItem>) : super() {
        this.context = context
        this.shoppingList.addAll(items.reversed())
    }

    constructor(context: Context) : super(){
        this.context = context
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
                R.layout.shopping_item_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shoppingList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = shoppingList[position]
        holder.tvName.text = item.itemName
        determineType(item, holder)
        holder.cbDone.isChecked = item.purchased
        setCheckBoxListener(holder, item)
        holder.tvQuantity.text = context.getString(R.string.quantity_x)+ item.itemQuantity
        holder.tvPrice.text = context.getString(R.string.cost_per_unit) + item.itemPrice
        holder.btnDelete.setOnClickListener {
            deleteItem(holder.adapterPosition)
        }
        holder.btnEdit.setOnClickListener {
                (context as MainActivity).showEditShoppingItemDialog(
                        item, holder.adapterPosition)
        }
    }

    private fun setCheckBoxListener(holder: ViewHolder, item: ShoppingItem) {
        holder.cbDone.setOnCheckedChangeListener { buttonView, isChecked ->
            item.purchased = holder.cbDone.isChecked
            Thread {
                AppDatabase.getInstance(context as MainActivity).shoppingDao().updateShoppingItem(item)
            }.start()
        }
    }

    private fun determineType(item: ShoppingItem, holder: ViewHolder) {
        when {
            item.itemType == context.getString(R.string.food) -> holder.imageIcon.setImageResource(R.drawable.food)
            item.itemType == context.getString(R.string.electronics) -> holder.imageIcon.setImageResource(R.drawable.electronics)
            item.itemType == context.getString(R.string.clothing) -> holder.imageIcon.setImageResource(R.drawable.clothing)
            item.itemType == context.getString(R.string.misc) -> holder.imageIcon.setImageResource(R.drawable.misc)
            else -> throw RuntimeException(context.getString(R.string.shopping_item_unspecified))
        }
    }

    private fun deleteItem(position: Int) {
        Thread{
            AppDatabase.getInstance(context).shoppingDao().deleteShoppingItem(
                    shoppingList[position]
            )

            (context as MainActivity).runOnUiThread{
                shoppingList.removeAt(position)
                notifyItemRemoved(position)
            }
        }.start()
    }

    fun deleteAll(){
        shoppingList.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvName = itemView.tvName
        val imageIcon = itemView.imageIcon
        val cbDone = itemView.cbDone
        val btnDelete = itemView.btnDelete
        val btnEdit = itemView.btnEdit
        val tvQuantity = itemView.tvQuantity
        val tvPrice = itemView.tvPrice
    }

    fun addShoppingItem(newItem: ShoppingItem){
        shoppingList.add(0, newItem)
        notifyItemInserted(0)
    }

    override fun onDismiss(position: Int) {
        deleteItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(shoppingList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun updateShoppingList(item: ShoppingItem, idx: Int) {
        shoppingList[idx] = item
        notifyItemChanged(idx)
    }
}