package com.example.jmin.shoppinglistapp

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.TextView
import kotlinx.android.synthetic.main.dialog_total_cost.view.*

class TotalCostDialog : DialogFragment(){

    private lateinit var tvTotalCost: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.total_cost_of_shopping_list))

        val rootView = requireActivity().layoutInflater.inflate(R.layout.dialog_total_cost, null)
        tvTotalCost = rootView.tvTotalCost

        builder.setView(rootView)

        val arguments = this.arguments
        if (arguments != null && arguments.containsKey(MainActivity.KEY_TOTAL_COST)) {
            val totalCost = arguments.getInt(
                    MainActivity.KEY_TOTAL_COST)
            tvTotalCost.text = getString(R.string.money_sign) + totalCost.toString()
        }

        builder.setPositiveButton(getString(R.string.ok)){
            dialog, witch -> // empty
        }

        return builder.create()
    }
    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            dialog.dismiss()
        }
    }
}
