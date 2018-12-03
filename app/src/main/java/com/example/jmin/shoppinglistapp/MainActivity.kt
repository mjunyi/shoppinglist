package com.example.jmin.shoppinglistapp

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.jmin.shoppinglistapp.adapter.ShoppingListAdapter
import com.example.jmin.shoppinglistapp.data.AppDatabase
import com.example.jmin.shoppinglistapp.data.ShoppingItem
import com.example.jmin.shoppinglistapp.touch.ShoppingListTouchHelperCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ShoppingItemDialog.ShoppingListHandler {
    companion object{
        val KEY_TOTAL_COST = "KEY_TOTAL_COST"
        val KEY_ITEM_TO_EDIT = "KEY_ITEM_TO_EDIT"
        val REQUEST_DETAILS = 1001
        val KEY_NAME = "KEY_NAME"
        val KEY_TYPE = "KEY_TYPE"
        val KEY_QUANTITY = "KEY_QUANTITY"
        val KEY_DESCRIPTION = "KEY_DESCRIPTION"
        val KEY_CHECKBOX = "KEY_CHECKBOX"
        val KEY_PRICE = "KEY_PRICE"
    }
    private lateinit var shoppingListAdapter: ShoppingListAdapter
    private var editIndex: Int = 0

    private var newPrice: Int? = -1
    private var newName: String? = ""
    private var newType: String? = ""
    private var newQuantity: Int? = -1
    private var newDescription:String? = ""
    private var newCheckbox: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        Thread {
            val shoppingList = AppDatabase.getInstance(this@MainActivity).shoppingDao().findAllShoppingItems()

            runOnUiThread {
                shoppingListAdapter = ShoppingListAdapter(this@MainActivity, shoppingList)
                recyclerShoppingList.adapter = shoppingListAdapter
                val callback = ShoppingListTouchHelperCallback(shoppingListAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerShoppingList)
            }
        }.start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_item -> {
                addShoppingItemActivity()
            }
            R.id.action_delete_all -> {
                deleteShoppingList()
            }
            R.id.action_show_total_cost ->{
                showTotalCostDialog(calculateTotalCost())
            }
        }
        return true
    }

    private fun runSecondActivity(){
        val myIntent = Intent()
        myIntent.setClass(this@MainActivity, AddShoppingItem::class.java)
        startActivityForResult(myIntent, REQUEST_DETAILS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode== REQUEST_DETAILS){
            if(resultCode == Activity.RESULT_OK){
                newName = data?.getStringExtra(KEY_NAME)
                newType = data?.getStringExtra(KEY_TYPE)
                newQuantity = data?.getIntExtra(KEY_QUANTITY, -1)
                newDescription = data?.getStringExtra(KEY_DESCRIPTION)
                newCheckbox = data?.getBooleanExtra(KEY_CHECKBOX, false)==true
                newPrice = data?.getIntExtra(KEY_PRICE, -1)

                shoppingListCreated(ShoppingItem(
                        null, newType.toString(), newName.toString(),
                        newQuantity.toString().toInt(), newDescription.toString(),
                        newCheckbox, newPrice.toString().toInt())
                )
            }
        }
    }

    private fun addShoppingItemActivity(){
        runSecondActivity()
    }

    public fun showEditShoppingItemDialog(shoppingItemToEdit: ShoppingItem, idx: Int) {
        editIndex = idx
        val editItemDialog = ShoppingItemDialog()
        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_TO_EDIT, shoppingItemToEdit)
        editItemDialog.arguments = bundle

        editItemDialog.show(supportFragmentManager,
                getString(R.string.edit_item_dialog))
    }

    public fun showTotalCostDialog(totalCost: Int) {
        val totalCostDialog = TotalCostDialog()
        val bundle = Bundle()
        bundle.putInt(KEY_TOTAL_COST, totalCost)
        totalCostDialog.arguments = bundle
        totalCostDialog.show(supportFragmentManager, getString(R.string.total_cost_dialog))
    }

    private fun calculateTotalCost():Int{
        var totalCost = 0
        val thread = Thread{
            val shoppingList = AppDatabase.getInstance(
                    this@MainActivity).shoppingDao().findAllShoppingItems()
            for(item in shoppingList){
                totalCost += item.itemQuantity*item.itemPrice
            }
        }
        thread.start()
        while(thread.isAlive){
        }
        return totalCost
    }

    private fun deleteShoppingList(){
        Thread {
            AppDatabase.getInstance(this@MainActivity).shoppingDao().deleteAll()
            runOnUiThread{
                shoppingListAdapter.deleteAll()
            }
        }.start()
    }

    override fun shoppingListCreated(item: ShoppingItem) {
        Thread {
            val id = AppDatabase.getInstance(
                    this@MainActivity).shoppingDao().insertShoppingItem(item)
            item.itemId = id
            runOnUiThread{
                shoppingListAdapter.addShoppingItem(item)
            }
        }.start()
    }

    override fun shoppingListUpdated(item: ShoppingItem) {
        val dbThread = Thread {
            AppDatabase.getInstance(this@MainActivity).shoppingDao().updateShoppingItem(item)
            runOnUiThread { shoppingListAdapter.updateShoppingList(item, editIndex) }
        }
        dbThread.start()
    }
}
