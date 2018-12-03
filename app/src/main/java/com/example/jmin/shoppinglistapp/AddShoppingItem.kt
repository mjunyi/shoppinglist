package com.example.jmin.shoppinglistapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.add_shopping_item.*

class AddShoppingItem : AppCompatActivity(){
    companion object {
        val KEY_NAME = "KEY_NAME"
        val KEY_TYPE = "KEY_TYPE"
        val KEY_QUANTITY = "KEY_QUANTITY"
        val KEY_DESCRIPTION = "KEY_DESCRIPTION"
        val KEY_CHECKBOX = "KEY_CHECKBOX"
        val KEY_PRICE = "KEY_PRICE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_shopping_item)

        btnAddNewItem.setOnClickListener {
            if(etShoppingItemName.text.isNotEmpty()&&etQuantity.text.isNotEmpty()
                        && etPrice.text.isNotEmpty()){
                var intentResult = Intent()
                putExtra(intentResult)
                setResult(Activity.RESULT_OK,intentResult)
                finish()
            }
            else{
                handleEmptyInput()
            }
        }
    }

    private fun putExtra(intentResult: Intent) {
        intentResult.putExtra(KEY_NAME, etShoppingItemName.text.toString())
        intentResult.putExtra(KEY_TYPE, spinner.selectedItem.toString())
        intentResult.putExtra(KEY_QUANTITY, etQuantity.text.toString().toInt())

        if (etItemDescription.text.isNotEmpty()) {
            intentResult.putExtra(KEY_DESCRIPTION,etItemDescription.text.toString())
        } else {
            intentResult.putExtra(KEY_DESCRIPTION, "")
        }
        intentResult.putExtra(KEY_CHECKBOX, cbDialogDone.isChecked)
        intentResult.putExtra(KEY_PRICE, etPrice.text.toString().toInt())
    }


    private fun handleEmptyInput() {
        if (etShoppingItemName.text.isEmpty()) {
            etShoppingItemName.error = getString(R.string.this_field_cannot_be_empty)
        }
        if (etQuantity.text.isEmpty()){
            etQuantity.error = getString(R.string.this_field_cannot_be_empty)
        }
        if (etPrice.text.isEmpty()){
            etPrice.error = getString(R.string.this_field_cannot_be_empty)
        }
    }

}