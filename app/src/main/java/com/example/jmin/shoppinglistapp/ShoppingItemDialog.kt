package com.example.jmin.shoppinglistapp

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import com.example.jmin.shoppinglistapp.data.ShoppingItem
import kotlinx.android.synthetic.main.dialog_shopping_item.*
import kotlinx.android.synthetic.main.dialog_shopping_item.view.*
import java.util.*

class ShoppingItemDialog : DialogFragment(){
    interface ShoppingListHandler{
        fun shoppingListCreated(item: ShoppingItem)
        fun shoppingListUpdated(item: ShoppingItem)
    }

    private lateinit var shoppingListHandler: ShoppingListHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(context is ShoppingListHandler){
            shoppingListHandler= context
        }
        else{
            throw RuntimeException(getString(R.string.runtime_execption))
        }
    }

    private lateinit var etShoppingItemName: EditText
    private lateinit var etShoppingItemType: Spinner
    private lateinit var etShoppingItemQuantity: EditText
    private lateinit var etShoppingItemDescription: EditText
    private lateinit var etShoppingItemCbDialogDone: CheckBox
    private lateinit var etShoppingItemPrice: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.edit_shopping_item))

        val rootView = requireActivity().layoutInflater.inflate(R.layout.dialog_shopping_item, null)

        bindRootView(rootView)

        builder.setView(rootView)
        val arguments = this.arguments
        if (arguments != null && arguments.containsKey(MainActivity.KEY_ITEM_TO_EDIT)) {
            val shoppingItem = arguments.getSerializable(
                    MainActivity.KEY_ITEM_TO_EDIT
            ) as ShoppingItem
            setDialogText(shoppingItem)
        }
        builder.setPositiveButton(getString(R.string.ok)){
            dialog, witch -> // empty
        }

        return builder.create()
    }

    private fun setDialogText(shoppingItem: ShoppingItem) {
        etShoppingItemName.setText(shoppingItem.itemName)
        etShoppingItemQuantity.setText(shoppingItem.itemQuantity.toString())
        etShoppingItemDescription.setText(shoppingItem.itemDescription)
        etShoppingItemCbDialogDone.isChecked = shoppingItem.purchased
        when {
            shoppingItem.itemType == getString(R.string.food) -> etShoppingItemType.setSelection(0)
            shoppingItem.itemType == getString(R.string.electronics) -> etShoppingItemType.setSelection(1)
            shoppingItem.itemType == getString(R.string.clothing) -> etShoppingItemType.setSelection(2)
            else -> etShoppingItemType.setSelection(3)
        }
        etShoppingItemPrice.setText(shoppingItem.itemPrice.toString())
    }

    private fun bindRootView(rootView: View) {
        etShoppingItemName = rootView.etShoppingItemName
        etShoppingItemType = rootView.spinner
        etShoppingItemQuantity = rootView.etQuantity
        etShoppingItemDescription = rootView.etItemDescription
        etShoppingItemCbDialogDone = rootView.cbDialogDone
        etShoppingItemPrice = rootView.etPrice
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etShoppingItemName.text.isNotEmpty() && etShoppingItemQuantity.text.isNotEmpty()
                            &&etShoppingItemPrice.text.isNotEmpty()) {
                val arguments = this.arguments
                if (arguments != null && arguments.containsKey(MainActivity.KEY_ITEM_TO_EDIT)) {
                    handleShoppingListEdit()
                }
                dialog.dismiss()
            } else {
                handleEmptyInput()
            }
        }
    }

    private fun handleEmptyInput() {
        if(etShoppingItemName.text.isEmpty()){
            etShoppingItemName.error = getString(R.string.this_field_cannot_be_empty)
        }

        if(etShoppingItemPrice.text.isEmpty()){
            etShoppingItemPrice.error = getString(R.string.this_field_cannot_be_empty)
        }

        if(etShoppingItemQuantity.text.isEmpty()){
            etShoppingItemQuantity.error = getString(R.string.this_field_cannot_be_empty)
        }
    }

    private fun handleShoppingListEdit() {
        val shoppingListToEdit = arguments?.getSerializable(
                MainActivity.KEY_ITEM_TO_EDIT
        ) as ShoppingItem
        shoppingListToEdit.itemName = etShoppingItemName.text.toString()

        setSpinnerChoice(shoppingListToEdit)

        shoppingListToEdit.itemQuantity = etShoppingItemQuantity.text.toString().toInt()
        shoppingListToEdit.itemDescription = etShoppingItemDescription.text.toString()
        shoppingListToEdit.purchased = etShoppingItemCbDialogDone.isChecked
        shoppingListToEdit.itemPrice = etShoppingItemPrice.text.toString().toInt()
        shoppingListHandler.shoppingListUpdated(shoppingListToEdit)
    }

    private fun setSpinnerChoice(shoppingListToEdit: ShoppingItem) {
        when {
            etShoppingItemType.selectedItemPosition == 0 -> shoppingListToEdit.itemType = getString(R.string.food)
            etShoppingItemType.selectedItemPosition == 1 -> shoppingListToEdit.itemType = getString(R.string.electronics)
            etShoppingItemType.selectedItemPosition == 2 -> shoppingListToEdit.itemType = getString(R.string.clothing)
            else -> shoppingListToEdit.itemType = getString(R.string.misc)
        }
    }
}
