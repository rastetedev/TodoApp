package com.rastete.todoapp.presentation.utils

import android.app.AlertDialog
import android.content.Context

fun createDialog(
    context: Context?,
    title: String,
    description: String,
    onPositiveButtonClicked: () -> Unit,
    onNegativeButtonClicked: () -> Unit
) {
    context?.let {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(description)
            .setPositiveButton("Yes") { dialog, _ ->
                onPositiveButtonClicked()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                onNegativeButtonClicked()
                dialog.dismiss()
            }
            .show()
    }


}