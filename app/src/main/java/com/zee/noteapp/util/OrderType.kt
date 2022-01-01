package com.zee.noteapp.util

sealed class OrderType {
    object Ascending : OrderType()
    object Descending : OrderType()
}
