package com.macaronsensationni.macaroniapp.callback

import android.graphics.drawable.Drawable

interface MacaroniCallback {

    fun onCookingItemClicked(index: Int, cookingDrawableSelected: Drawable) {}
}