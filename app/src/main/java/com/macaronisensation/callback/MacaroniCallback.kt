package com.macaronisensation.callback

import android.graphics.drawable.Drawable

interface MacaroniCallback {

    fun onCookingItemClicked(index: Int, cookingDrawableSelected: Drawable) {}
}