package com.example.attendanceapp.core.utils

import java.text.SimpleDateFormat
import java.util.*


fun getTodayToday(): String{
    return getYear().toString()+"-"+ getMonth()+"-"+ getDay()

}

fun getDay(): Int{
    val sdf = SimpleDateFormat("dd")
    return sdf.format(Date()).toInt()
}

fun getMonth(): Int{
    val sdf = SimpleDateFormat("M")
    return sdf.format(Date()).toInt()
}

fun getYear(): Int{
    val sdf = SimpleDateFormat("yyyy")
    return sdf.format(Date()).toInt()
}

fun getTime(): String{
    val sdf = SimpleDateFormat("HH:mm:ss")
    return sdf.format(Date()).toString()
}
