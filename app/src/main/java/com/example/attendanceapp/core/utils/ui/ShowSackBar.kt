package com.example.attendanceapp.core.utils.ui

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

fun Fragment.showLongSnackBar(contextView: View, message: String){
    Snackbar.make(contextView, message, Snackbar.LENGTH_SHORT)
        .show()

}

fun Fragment.showLongToast(message: String){
    Toast.makeText(this.activity, message, Toast.LENGTH_LONG).show()
}

fun stringFromTl(tl: TextInputLayout) = tl.editText!!.text.toString()
