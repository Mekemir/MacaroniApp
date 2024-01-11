package com.macaroni.macaroniapp

import android.graphics.drawable.Drawable

data class CookingItemData(
    val levelTaps: Int,
    val drawable: Drawable,
    val name: String,
    val listOfPastas: ArrayList<Drawable> = arrayListOf(),
    val incorrectPasta: String,
    val isLocked: Boolean
)
