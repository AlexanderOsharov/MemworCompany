package com.shurik.memwor_24.memwor.content.module_adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.shurik.memwor_24.R

class PopupMessage(val context: Context, val message: String, val onConfirm: () -> Unit) {

    private val dialog: AlertDialog

    init {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.popup_message, null)

        view.findViewById<TextView>(R.id.messageTextView).text = message

        view.findViewById<Button>(R.id.confirmButton).setOnClickListener {
            onConfirm.invoke()
            dismiss()
        }

        view.findViewById<Button>(R.id.cancelButton).setOnClickListener {
            dismiss()
        }

        dialog = AlertDialog.Builder(context)
            .setView(view)
            .create()
    }

    fun show() {
        dialog.show()
    }

    private fun dismiss() {
        dialog.dismiss()
    }
}